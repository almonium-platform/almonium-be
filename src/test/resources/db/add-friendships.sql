-- Populating the user table
INSERT INTO user_core (id, email, email_verified, username, registered, setup_step)
VALUES ('01956cde-a541-7ac1-8b32-2896d096ecdf', 'user1@example.com', true, 'user1', NOW(), 'COMPLETED'),
       ('01956cde-d6dd-7aca-bd07-e5c29cadf093', 'user2@example.com', true, 'user2', NOW(), 'COMPLETED');

-- Populating the friendship table
INSERT INTO friendship (id, requester_id, requestee_id, created_at, updated_at, status)
VALUES ('01956ce2-34fa-71f2-97b1-dacd41dbaba1', '01956cde-a541-7ac1-8b32-2896d096ecdf',
        '01956cde-d6dd-7aca-bd07-e5c29cadf093', NOW(), NOW(), 'FRIENDS');
