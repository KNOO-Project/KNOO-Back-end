CREATE TABLE user
(
    user_id           BIGINT       NOT NULL AUTO_INCREMENT,
    username          VARCHAR(20)  NOT NULL UNIQUE,
    password          VARCHAR(255) NOT NULL,
    name              VARCHAR(20)  NOT NULL UNIQUE,
    email             VARCHAR(30)  NOT NULL UNIQUE,
    join_date         VARCHAR(50)  NOT NULL,
    email_verify      VARCHAR(15)  NOT NULL,
    verification_code VARCHAR(50)  NOT NULL,
    campus            VARCHAR(20),
    major             VARCHAR(50),
    PRIMARY KEY (user_id)
);

CREATE TABLE role
(
    user_id   BIGINT      NOT NULL,
    role_name VARCHAR(50) NOT NULL,
    PRIMARY KEY (user_id, role_name),
    FOREIGN KEY (user_id) REFERENCES user (user_id) ON DELETE CASCADE
);

CREATE TABLE post
(
    post_id        BIGINT        NOT NULL AUTO_INCREMENT,
    post_title     VARCHAR(50)   NOT NULL,
    post_content   VARCHAR(4096) NOT NULL,
    post_category  VARCHAR(50)   NOT NULL,
    post_date      VARCHAR(50)   NOT NULL,
    anonymous      BOOLEAN       NOT NULL,
    comments_count INT           NOT NULL DEFAULT 0,
    likes_count    INT           NOT NULL DEFAULT 0,
    user_id        BIGINT,
    PRIMARY KEY (post_id),
    FOREIGN KEY (user_id) REFERENCES user (user_id) ON DELETE CASCADE
);

CREATE TABLE post_like
(
    post_like_id   BIGINT      NOT NULL AUTO_INCREMENT,
    post_like_date VARCHAR(50) NOT NULL,
    user_id        BIGINT,
    post_id        BIGINT      NOT NULL,
    PRIMARY KEY (post_like_id),
    FOREIGN KEY (user_id) REFERENCES user (user_id) ON DELETE SET NULL,
    FOREIGN KEY (post_id) REFERENCES post (post_id) ON DELETE CASCADE
);

CREATE TABLE comment
(
    comment_id        BIGINT       NOT NULL AUTO_INCREMENT,
    comment_content   VARCHAR(500) NOT NULL,
    comment_date      VARCHAR(50)  NOT NULL,
    likes_count       INT          NOT NULL DEFAULT 0,
    deleted           BIT          NOT NULL DEFAULT FALSE,
    parent_comment_id BIGINT,
    user_id           BIGINT,
    post_id           BIGINT       NOT NULL,
    PRIMARY KEY (comment_id),
    FOREIGN KEY (user_id) REFERENCES user (user_id) ON DELETE SET NULL,
    FOREIGN KEY (post_id) REFERENCES post (post_id) ON DELETE CASCADE,
    FOREIGN KEY (parent_comment_id) REFERENCES comment (comment_id)
);

CREATE TABLE comment_like
(
    comment_like_id BIGINT NOT NULL AUTO_INCREMENT,
    user_id         BIGINT,
    comment_id      BIGINT NOT NULL,
    PRIMARY KEY (comment_like_id),
    FOREIGN KEY (user_id) REFERENCES user (user_id) ON DELETE SET NULL,
    FOREIGN KEY (comment_id) REFERENCES comment (comment_id) ON DELETE CASCADE
);

CREATE TABLE image
(
    image_id   BIGINT       NOT NULL AUTO_INCREMENT,
    image_name VARCHAR(100) NOT NULL,
    image_size BIGINT,
    post_id    BIGINT       NOT NULL,
    PRIMARY KEY (image_id),
    FOREIGN KEY (post_id) REFERENCES post (post_id) ON DELETE CASCADE
);