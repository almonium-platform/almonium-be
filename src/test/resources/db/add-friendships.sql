-- Populating the user table
INSERT INTO user_core (id, email, email_verified, username, registered, setup_step)
VALUES (1, 'user1@example.com', true, 'user1', NOW(), 'COMPLETED'),
       (2, 'user2@example.com', true, 'user2', NOW(), 'COMPLETED');

-- Populating the friendship table
INSERT INTO friendship (requester_id, requestee_id, created_at, updated_at, status)
VALUES (1, 2, NOW(), NOW(), 'FRIENDS');
