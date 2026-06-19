INSERT INTO user_profile (first_name, last_name, address, profile_img_url, profile_status)
VALUES
    ('Admin', 'User', 'Admin Street 1', NULL, 'UNVERIFIED'),
    ('Anna', 'Müller', 'Berlin 12', NULL, 'UNVERIFIED'),
    ('James', 'Bond', 'London HQ', NULL, 'UNVERIFIED'),
    ('Markus', 'Schumacher', 'Markusstrasse 10, 8472 Seuzach', NULL, 'UNVERIFIED');

-- Initial data for account table
-- Passwords are Bcrypt hashes of the password 'pizza'.
INSERT INTO account (first_name, last_name, username, password_hash, email, role, profile_id) VALUES
  ('Admin', 'User', 'admin', '$2y$05$1zMf9eQgDCWqewuTi0J6ze325QbprMBdmvROTwJ8dXqseRJy6hKwu', 'admin@example.com', 'USER', 1),
  ('Anna', 'Müller', 'anna', '$2y$05$1zMf9eQgDCWqewuTi0J6ze325QbprMBdmvROTwJ8dXqseRJy6hKwu', 'anna@example.com', 'ADMIN', 2),
  ('James', 'Bond', 'james', '$2y$05$1zMf9eQgDCWqewuTi0J6ze325QbprMBdmvROTwJ8dXqseRJy6hKwu', 'james@example.com', 'COACH', 3),
  ('Markus', 'Schumacher', 'maere77', '$2y$05$1zMf9eQgDCWqewuTi0J6ze325QbprMBdmvROTwJ8dXqseRJy6hKwu', 'markus.schumacher@axa.ch', 'DEVELOPER', 4);

INSERT INTO groups (name, owner_id)
VALUES
    ('Family', 1),
    ('Friends', 2);

INSERT INTO media (media, media_status, group_id, account_id)
VALUES
    ('family_photo.jpg', 'PUBLIC', 1, 1),
    ('vacation_video.mp4', 'PUBLIC', 1, 2),
    ('party_photo.png', 'PUBLIC', 2, 3);

INSERT INTO members_groups (group_id, member_id)
VALUES
    (1, 1),
    (1, 2),
    (1, 3),
    (2, 2),
    (2, 3);