-- Populating the user table
INSERT INTO user_core (id, email, username, registered)
VALUES (1, 'user1@example.com', 'user1', NOW()),
       (2, 'user2@example.com', 'user2', NOW());

-- Populating the friendship table
INSERT INTO friendship (requester_id, requestee_id, created_at, updated_at, status)
VALUES (1, 2, NOW(), NOW(), 'FRIENDS');
