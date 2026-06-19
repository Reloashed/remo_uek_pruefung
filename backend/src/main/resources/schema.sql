CREATE TABLE user_profile (
                              id BIGINT AUTO_INCREMENT PRIMARY KEY,
                              first_name VARCHAR(255),
                              last_name VARCHAR(255),
                              address VARCHAR(255),
                              profile_img_url VARCHAR(512),
                              profile_status VARCHAR(255) NOT NULL
);

CREATE TABLE account
(
    id            BIGINT AUTO_INCREMENT PRIMARY KEY,
    first_name     VARCHAR(255),
    last_name      VARCHAR(255),
    username      VARCHAR(255) NOT NULL UNIQUE,
    password_hash VARCHAR(100) NOT NULL,
    email         VARCHAR(255) UNIQUE,
    login_token   VARCHAR(255) DEFAULT NULL,
    role          VARCHAR(50) NOT NULL,

    profile_id    BIGINT UNIQUE NOT NULL,

    liked_media BIGINT,
    owned_media BIGINT,

    CONSTRAINT fk_account_profile
        FOREIGN KEY (profile_id)
            REFERENCES user_profile(id)
            ON DELETE CASCADE
);

CREATE UNIQUE INDEX IF NOT EXISTS idx_account_username ON account(username);
CREATE INDEX IF NOT EXISTS idx_account_email ON account(email);

CREATE TABLE groups (
                        id BIGINT AUTO_INCREMENT PRIMARY KEY,
                        name VARCHAR(255) NOT NULL,
                        owner_id BIGINT,
                        CONSTRAINT fk_group_owner
                            FOREIGN KEY (owner_id)
                                REFERENCES account(id)
);

CREATE TABLE media (
                       id BIGINT AUTO_INCREMENT PRIMARY KEY,
                       media VARCHAR(255) NOT NULL,
                        media_status VARCHAR(255) NOT NULL,
                       group_id BIGINT,
                        account_id BIGINT,
                        liked_by BIGINT,
                       CONSTRAINT fk_media_group
                           FOREIGN KEY (group_id)
                               REFERENCES groups(id)
);

CREATE TABLE members_groups (
                                group_id BIGINT NOT NULL,
                                member_id BIGINT NOT NULL,

                                PRIMARY KEY (group_id, member_id),

                                CONSTRAINT fk_members_groups_group
                                    FOREIGN KEY (group_id)
                                        REFERENCES groups(id),

                                CONSTRAINT fk_members_groups_account
                                    FOREIGN KEY (member_id)
                                        REFERENCES account(id)
);

CREATE TABLE media_likes (
                        media_id BIGINT NOT NULL,
                        account_id BIGINT NOT NULL,

                        PRIMARY KEY (media_id, account_id),

                        CONSTRAINT fk_media_likes_media
                            FOREIGN KEY (media_id)
                                REFERENCES media(id),

                        CONSTRAINT fk_media_likes_account
                            FOREIGN KEY (account_id)
                                REFERENCES account(id)
);