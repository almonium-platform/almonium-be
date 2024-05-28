-- Populating the user table
INSERT INTO auth (id, password, email, username, registered)
VALUES (1, 'password1', 'user1@example.com', 'user1', NOW()),
       (2, 'password2', 'user2@example.com', 'user2', NOW());

-- Populating the friendship table
INSERT INTO friendship (requester_id, requestee_id, created, updated, status)
VALUES (1, 2, NOW(), NOW(), 'FRIENDS');
