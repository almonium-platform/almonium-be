#!/bin/bash
cd /home/kuzanoleg/almonium

# Pull latest code
git reset --hard
git clean -fd
git pull

# Generate a unique deploy version (timestamp-based)
export DEPLOY_VERSION=$(date +%Y%m%d%H%M)
echo "Deploying version: $DEPLOY_VERSION"

# Build and start with the new version
docker-compose build --no-cache
docker-compose up -d --no-deps --build

# Wait for the new version to start
sleep 10

# Remove old container
docker image prune -f