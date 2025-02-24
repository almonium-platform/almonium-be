#!/bin/bash
cd /home/kuzanoleg/almonium

# Pull latest code
git reset --hard
git clean -fd
git pull

# Build new version
docker-compose build --no-cache

# Start new version before stopping the old one
docker-compose up -d --no-deps

# Wait a few seconds to ensure the new version is running
sleep 5

# Remove old container
docker system prune -f
