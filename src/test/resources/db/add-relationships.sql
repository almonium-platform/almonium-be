-- Populating the user table
INSERT INTO user_core (id, email, email_verified, username, registered, setup_step)
VALUES ('01956cde-a541-7ac1-8b32-2896d096ec01', 'user1@example.com', true, 'user1', NOW(), 'COMPLETED'),
       ('01956cde-d6dd-7aca-bd07-e5c29cadf093', 'user2@example.com', true, 'user2', NOW(), 'COMPLETED'),
       -- a stranger, so a handle search has someone to report no relationship for
       ('01956cde-e7c1-7b0a-9f21-3b41f0a5c0d3', 'user3@example.com', true, 'user3', NOW(), 'COMPLETED');

INSERT INTO profile (id, avatar_url, hidden, last_login)
VALUES ('01956cde-a541-7ac1-8b32-2896d096ec01', NULL, false, NOW()),
       ('01956cde-d6dd-7aca-bd07-e5c29cadf093', '/assets/img/avatars/default/stag.png', false, NOW()),
       ('01956cde-e7c1-7b0a-9f21-3b41f0a5c0d3', NULL, false, NOW());

INSERT INTO plan_subscription (id, user_id, plan_id, status, start_date, updated_at)
VALUES ('01956cdf-3c54-7db4-a850-95d764ac4dc2', '01956cde-d6dd-7aca-bd07-e5c29cadf093', 2, 'ACTIVE', NOW(), NOW());

-- Populating the relationship table
INSERT INTO relationship (id, requester_id, requestee_id, created_at, updated_at, status)
VALUES ('01956ce2-34fa-71f2-97b1-dacd41dbaba1', '01956cde-a541-7ac1-8b32-2896d096ec01',
        '01956cde-d6dd-7aca-bd07-e5c29cadf093', NOW(), NOW(), 'FRIENDS');

-- What each person studies, so a People row can say who they are. user3 keeps one language set
-- aside, to prove an inactive learner never reaches the panel.
INSERT INTO learner (id, user_id, language, self_reported_level, active, created_at)
VALUES ('01956ce3-1111-7000-8000-000000000001', '01956cde-d6dd-7aca-bd07-e5c29cadf093', 'ES', 'A2', true, NOW()),
       ('01956ce3-1111-7000-8000-000000000002', '01956cde-d6dd-7aca-bd07-e5c29cadf093', 'DE', 'B1', true, NOW()),
       ('01956ce3-1111-7000-8000-000000000003', '01956cde-e7c1-7b0a-9f21-3b41f0a5c0d3', 'FR', 'A1', false, NOW());
