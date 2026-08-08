#!/usr/bin/env python3
"""One-off bootstrap: grant or revoke the Firebase `admin` custom claim.

The backend derives ROLE_ADMIN purely from this claim (see
FirebaseAdminAuthGateway.java / FirebaseSessionService.java) - nothing in
Postgres represents adminhood, so this script is the only way to create the
first admin in a Firebase project. Run it once per project (once for your
local/staging Firebase project, once for prod), not once per docker reset.

Usage:
    pip install firebase-admin
    python scripts/set_firebase_admin_claim.py you@example.com
    python scripts/set_firebase_admin_claim.py you@example.com --remove
    python scripts/set_firebase_admin_claim.py you@example.com --key-file /path/to/prod-service-account.json

By default it reads the same credential the app itself uses locally
(GOOGLE_SERVICE_ACCOUNT_KEY_BASE64 in .env at the repo root), so it targets
whichever Firebase project your .env currently points at. Pass --key-file to
target a different project (e.g. prod) without touching your local .env.
"""

import argparse
import base64
import json
import sys
from pathlib import Path

try:
    import firebase_admin
    from firebase_admin import auth, credentials
except ImportError:
    sys.exit("Missing dependency. Run: pip install firebase-admin")

REPO_ROOT = Path(__file__).resolve().parent.parent
ENV_FILE = REPO_ROOT / ".env"
ENV_VAR = "GOOGLE_SERVICE_ACCOUNT_KEY_BASE64"


def load_service_account_from_env_file() -> dict:
    if not ENV_FILE.exists():
        sys.exit(f"{ENV_FILE} not found. Pass --key-file instead, or create .env from .env.template.")

    for line in ENV_FILE.read_text().splitlines():
        line = line.strip()
        if not line or line.startswith("#") or "=" not in line:
            continue
        key, _, value = line.partition("=")
        if key.strip() == ENV_VAR:
            value = value.strip().strip('"').strip("'")
            if not value or value == "replace-me":
                sys.exit(f"{ENV_VAR} in {ENV_FILE} is not set. Pass --key-file instead.")
            return json.loads(base64.b64decode(value))

    sys.exit(f"{ENV_VAR} not found in {ENV_FILE}. Pass --key-file instead.")


def load_service_account(key_file: str | None) -> dict:
    if key_file:
        path = Path(key_file).expanduser()
        if not path.exists():
            sys.exit(f"Key file not found: {path}")
        return json.loads(path.read_text())
    return load_service_account_from_env_file()


def main() -> None:
    parser = argparse.ArgumentParser(description=__doc__, formatter_class=argparse.RawDescriptionHelpFormatter)
    parser.add_argument("email", help="Email of the Firebase user to grant/revoke admin for")
    parser.add_argument("--remove", action="store_true", help="Revoke admin instead of granting it")
    parser.add_argument("--key-file", help="Path to a service account JSON key (overrides .env)")
    args = parser.parse_args()

    service_account = load_service_account(args.key_file)
    project_id = service_account.get("project_id", "<unknown>")

    firebase_admin.initialize_app(credentials.Certificate(service_account))

    try:
        user = auth.get_user_by_email(args.email)
    except auth.UserNotFoundError:
        sys.exit(f"No Firebase user with email {args.email} in project {project_id}. Sign up first, then re-run.")

    claims = dict(user.custom_claims or {})
    if args.remove:
        claims.pop("admin", None)
    else:
        claims["admin"] = True

    auth.set_custom_user_claims(user.uid, claims)
    auth.revoke_refresh_tokens(user.uid)  # forces the new claim to take effect on next sign-in

    action = "Revoked" if args.remove else "Granted"
    print(f"{action} admin for {args.email} (uid={user.uid}) in project {project_id}.")
    print(f"Resulting custom claims: {claims}")
    print("Existing sessions were revoked - sign in again for it to take effect.")


if __name__ == "__main__":
    main()
