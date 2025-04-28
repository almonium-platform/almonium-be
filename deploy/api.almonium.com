# /etc/nginx/sites-available/almonium.conf
upstream almonium_backend {
    include /home/almonium/infra/current/upstream.conf;
}

server {
    server_name api.almonium.com;

    # Let Certbot answer its HTTP-01 challenges
    location /.well-known/acme-challenge/ {
        root /var/www/certbot;
    }

    # Temporary upstream until we install TLS
    location / {
        proxy_pass http://almonium_backend;
        proxy_set_header Host $host;
        proxy_http_version 1.1;
        proxy_set_header Connection "";
    }

    listen 443 ssl; # managed by Certbot
    ssl_certificate /etc/letsencrypt/live/api.almonium.com/fullchain.pem; # managed by Certbot
    ssl_certificate_key /etc/letsencrypt/live/api.almonium.com/privkey.pem; # managed by Certbot
    include /etc/letsencrypt/options-ssl-nginx.conf; # managed by Certbot
    ssl_dhparam /etc/letsencrypt/ssl-dhparams.pem; # managed by Certbot

}

server {
    if ($host = api.almonium.com) {
        return 301 https://$host$request_uri;
    } # managed by Certbot


    listen 80;
    server_name api.almonium.com;
    return 404; # managed by Certbot
}
