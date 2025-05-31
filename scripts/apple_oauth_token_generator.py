import jwt
import os
import time
import base64

TEAM_ID = os.getenv("APPLE_TEAM_ID")
CLIENT_ID = os.getenv("APPLE_CLIENT_ID")
KEY_ID = os.getenv("APPLE_KEY_ID")
PRIVATE_KEY_CONTENT = os.getenv("APPLE_PRIVATE_KEY_CONTENT")

def generate_token():
    if not all([TEAM_ID, CLIENT_ID, KEY_ID, PRIVATE_KEY_CONTENT]):
        missing = [
            var_name for var_name, var_val in [
                ("APPLE_TEAM_ID", TEAM_ID),
                ("APPLE_CLIENT_ID", CLIENT_ID),
                ("APPLE_KEY_ID", KEY_ID),
                ("APPLE_PRIVATE_KEY_CONTENT", PRIVATE_KEY_CONTENT)
            ] if not var_val
        ]
        print(f"Error: Missing environment variables: {', '.join(missing)}")
        return

    private_key = PRIVATE_KEY_CONTENT
    timestamp_now = int(time.time())
    timestamp_exp = timestamp_now + 15777000  # ~6 months

    data = {
        "iss": TEAM_ID,
        "iat": timestamp_now,
        "exp": timestamp_exp,
        "aud": "https://appleid.apple.com",
        "sub": CLIENT_ID,
    }

    try:
        token = jwt.encode(
            payload=data,
            key=private_key,
            algorithm="ES256",
            headers={"kid": KEY_ID}
        )
        encoded_token = base64.b64encode(token.encode('utf-8')).decode('utf-8')

        print("Successfully generated Apple Client Secret (JWT).")
        print("Copy the Base64 encoded token below and decode it locally:")
        print(encoded_token)
    except Exception as e:
        print(f"Error generating token: {e}")

if __name__ == "__main__":
    generate_token()
