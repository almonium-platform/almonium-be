import jwt
import os
import time
from dotenv import load_dotenv

load_dotenv()

TEAM_ID = os.getenv("APPLE_TEAM_ID")
CLIENT_ID = os.getenv("APPLE_CLIENT_ID")
KEY_ID = os.getenv("APPLE_KEY_ID")
PRIVATE_KEY_PATH = os.getenv("APPLE_PRIVATE_KEY_PATH")


def generate_token():
    with open(PRIVATE_KEY_PATH, "r") as f:
        private_key = f.read()
    timestamp_now = int(time.time())
    timestamp_exp = timestamp_now + 15777000  # ~6 months
    data = {
        "iss": TEAM_ID,
        "iat": timestamp_now,
        "exp": timestamp_exp,
        "aud": "https://appleid.apple.com",
        "sub": CLIENT_ID,
    }
    token = jwt.encode(
        payload=data,
        key=private_key.encode('utf-8'),
        algorithm="ES256",
        headers={"kid": KEY_ID}
    )
    print(token)


if __name__ == "__main__":
    generate_token()
