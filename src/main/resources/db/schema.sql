CREATE TABLE IF NOT EXISTS users
(
    user_id           BIGINT       NOT NULL AUTO_INCREMENT,
    username          VARCHAR(20)  NOT NULL UNIQUE,
    password          VARCHAR(255) NOT NULL,
    name              VARCHAR(20)  NOT NULL UNIQUE,
    email             VARCHAR(30)  NOT NULL UNIQUE,
    join_date         DATETIME(6)  NOT NULL,
    email_verify      VARCHAR(15)  NOT NULL,
    verification_code VARCHAR(50)  NOT NULL,
    campus            VARCHAR(20),
    major             VARCHAR(50),
    PRIMARY KEY (user_id)
);

CREATE TABLE IF NOT EXISTS role
(
    user_id   BIGINT      NOT NULL,
    role_name VARCHAR(50) NOT NULL,
    PRIMARY KEY (user_id, role_name),
    FOREIGN KEY (user_id) REFERENCES users (user_id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS post
(
    post_id        BIGINT        NOT NULL AUTO_INCREMENT,
    post_title     VARCHAR(50)   NOT NULL,
    post_content   VARCHAR(4096) NOT NULL,
    post_category  VARCHAR(50)   NOT NULL,
    post_date      DATETIME(6)   NOT NULL,
    anonymous      BOOLEAN       NOT NULL,
    comments_count INT           NOT NULL DEFAULT 0,
    likes_count    INT           NOT NULL DEFAULT 0,
    scraps_count   INT           NOT NULL DEFAULT 0,
    user_id        BIGINT,
    PRIMARY KEY (post_id),
    FOREIGN KEY (user_id) REFERENCES users (user_id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS post_like
(
    post_like_id   BIGINT      NOT NULL AUTO_INCREMENT,
    post_like_date DATETIME(6) NOT NULL,
    user_id        BIGINT,
    post_id        BIGINT      NOT NULL,
    PRIMARY KEY (post_like_id),
    FOREIGN KEY (user_id) REFERENCES users (user_id) ON DELETE SET NULL,
    FOREIGN KEY (post_id) REFERENCES post (post_id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS scrap
(
    scrap_id   BIGINT      NOT NULL AUTO_INCREMENT,
    scrap_date DATETIME(6) NOT NULL,
    user_id    BIGINT,
    post_id    BIGINT      NOT NULL,
    PRIMARY KEY (scrap_id),
    FOREIGN KEY (user_id) REFERENCES users (user_id) ON DELETE SET NULL,
    FOREIGN KEY (post_id) REFERENCES post (post_id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS comment
(
    comment_id        BIGINT       NOT NULL AUTO_INCREMENT,
    comment_content   VARCHAR(500) NOT NULL,
    comment_date      DATETIME(6)  NOT NULL,
    likes_count       INT          NOT NULL DEFAULT 0,
    deleted           BIT          NOT NULL DEFAULT FALSE,
    parent_comment_id BIGINT,
    user_id           BIGINT,
    post_id           BIGINT       NOT NULL,
    PRIMARY KEY (comment_id),
    FOREIGN KEY (user_id) REFERENCES users (user_id) ON DELETE SET NULL,
    FOREIGN KEY (post_id) REFERENCES post (post_id) ON DELETE CASCADE,
    FOREIGN KEY (parent_comment_id) REFERENCES comment (comment_id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS comment_like
(
    comment_like_id BIGINT NOT NULL AUTO_INCREMENT,
    user_id         BIGINT,
    comment_id      BIGINT NOT NULL,
    PRIMARY KEY (comment_like_id),
    FOREIGN KEY (user_id) REFERENCES users (user_id) ON DELETE SET NULL,
    FOREIGN KEY (comment_id) REFERENCES comment (comment_id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS image
(
    image_id   BIGINT       NOT NULL AUTO_INCREMENT,
    image_name VARCHAR(100) NOT NULL,
    image_size BIGINT,
    post_id    BIGINT       NOT NULL,
    PRIMARY KEY (image_id),
    FOREIGN KEY (post_id) REFERENCES post (post_id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS restaurant
(
    restaurant_id   BIGINT       NOT NULL AUTO_INCREMENT,
    restaurant_name VARCHAR(50)  NOT NULL,
    address         VARCHAR(100) NOT NULL,
    latitude        VARCHAR(20)  NOT NULL,
    longitude       VARCHAR(20)  NOT NULL,
    cuisine_type    VARCHAR(20)  NOT NULL,
    campus          VARCHAR(10)  NOT NULL,
    url             VARCHAR(500),
    PRIMARY KEY (restaurant_id)
);

CREATE TABLE IF NOT EXISTS notification
(
    notification_id          BIGINT       NOT NULL AUTO_INCREMENT,
    notification_description VARCHAR(100) NOT NULL,
    notification_type        VARCHAR(20)  NOT NULL,
    `read`                   BOOLEAN      NOT NULL DEFAULT FALSE,
    notification_date        DATETIME(6)  NOT NULL,
    user_id                  BIGINT       NOT NULL,
    PRIMARY KEY (notification_id),
    FOREIGN KEY (user_id) REFERENCES users (user_id) ON DELETE CASCADE
);
