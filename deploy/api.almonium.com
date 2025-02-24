server {
    listen 443 ssl; # managed by Certbot
    server_name api.almonium.com;

    ssl_certificate /etc/letsencrypt/live/api.almonium.com/fullchain.pem; # managed by Certbot
    ssl_certificate_key /etc/letsencrypt/live/api.almonium.com/privkey.pem; # managed by Certbot
    include /etc/letsencrypt/options-ssl-nginx.conf; # managed by Certbot
    ssl_dhparam /etc/letsencrypt/ssl-dhparams.pem; # managed by Certbot

    location / {
        proxy_pass http://127.0.0.1:9998;
        proxy_http_version 1.1;
        proxy_set_header Upgrade $http_upgrade;
        proxy_set_header Connection "upgrade";
        proxy_set_header Host $host;
        proxy_cache_bypass $http_upgrade;
    }

    location /.well-known/acme-challenge/ {
        root /var/www/certbot;
        allow all;
    }
}

server {
    if ($host = api.almonium.com) {
        return 301 https://$host$request_uri;
    } # managed by Certbot

    listen 80;
    server_name api.almonium.com;
    return 404; # managed by Certbot
}