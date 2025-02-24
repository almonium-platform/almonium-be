#!/bin/bash
cd /home/kuzanoleg/almonium
git reset --hard
git clean -fd
git pull
sudo systemctl restart almonium.service