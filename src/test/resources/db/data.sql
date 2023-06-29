INSERT INTO users(username, password, name, email, join_date, email_verify, verification_code)
VALUES ('test1234', 'test1234', 'test', 'test@test.com', NOW(), 'ENABLE', 'random_code');

INSERT INTO post(post_title, post_content, post_category, post_date, anonymous, comments_count, likes_count, user_id)
VALUES ('test', 'test', 'FREE', NOW(), 0, 0, 0, 1);