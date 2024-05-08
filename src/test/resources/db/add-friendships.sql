-- Populating the user table
INSERT INTO auth (id, provider_user_id, password, provider, email, username, registered)
VALUES (1, 'provider1', 'password1', 'FACEBOOK', 'user1@example.com', 'user1', NOW()),
       (2, 'provider2', 'password2', 'GOOGLE', 'user2@example.com', 'user2', NOW());

-- Populating the friendship table
INSERT INTO friendship (requester_id, requestee_id, created, updated, status)
VALUES (1, 2, NOW(), NOW(), 'F');
