upstream almonium_backend {
    server 127.0.0.1:9998;
    server 127.0.0.1:9999 backup;
}

server {
    listen 443 ssl;
    server_name api.almonium.com;

    ssl_certificate /etc/letsencrypt/live/api.almonium.com/fullchain.pem;
    ssl_certificate_key /etc/letsencrypt/live/api.almonium.com/privkey.pem;
    include /etc/letsencrypt/options-ssl-nginx.conf;
    ssl_dhparam /etc/letsencrypt/ssl-dhparams.pem;

    location / {
        proxy_pass http://almonium_backend;
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
    }
    listen 80;
    server_name api.almonium.com;
    return 404;
}
