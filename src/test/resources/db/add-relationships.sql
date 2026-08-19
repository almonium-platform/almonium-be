-- Populating the user table
INSERT INTO user_core (id, email, email_verified, username, registered, setup_step)
VALUES ('01956cde-a541-7ac1-8b32-2896d096ec01', 'user1@example.com', true, 'user1', NOW(), 'COMPLETED'),
       ('01956cde-d6dd-7aca-bd07-e5c29cadf093', 'user2@example.com', true, 'user2', NOW(), 'COMPLETED');

INSERT INTO profile (id, avatar_url, hidden, last_login)
VALUES ('01956cde-a541-7ac1-8b32-2896d096ec01', NULL, false, NOW()),
       ('01956cde-d6dd-7aca-bd07-e5c29cadf093', '/assets/img/avatars/default/stag.png', false, NOW());

INSERT INTO plan_subscription (id, user_id, plan_id, status, start_date, updated_at)
VALUES ('01956cdf-3c54-7db4-a850-95d764ac4dc2', '01956cde-d6dd-7aca-bd07-e5c29cadf093', 2, 'ACTIVE', NOW(), NOW());

-- Populating the relationship table
INSERT INTO relationship (id, requester_id, requestee_id, created_at, updated_at, status)
VALUES ('01956ce2-34fa-71f2-97b1-dacd41dbaba1', '01956cde-a541-7ac1-8b32-2896d096ec01',
        '01956cde-d6dd-7aca-bd07-e5c29cadf093', NOW(), NOW(), 'FRIENDS');
