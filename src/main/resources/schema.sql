DROP TABLE IF EXISTS users CASCADE;
DROP TABLE IF EXISTS items CASCADE;
DROP TABLE IF EXISTS comments CASCADE;
DROP TABLE IF EXISTS bookings CASCADE;

CREATE TABLE IF NOT EXISTS users
(
    user_id   bigint GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    user_name VARCHAR(255) NOT NULL,
    email     VARCHAR(255) NOT NULL UNIQUE
);

CREATE TABLE IF NOT EXISTS items
(
    item_id          BIGINT GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    item_name        VARCHAR(200) NOT NULL,
    item_description VARCHAR(200) NOT NULL,
    is_available     BOOLEAN      NOT NULL,
    owner_id         BIGINT       NOT NULL REFERENCES users (user_id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS comments
(
    comment_id      BIGINT GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    comment_text    VARCHAR(200) NOT NULL,
    author_id       BIGINT       NOT NULL REFERENCES users (user_id) ON DELETE CASCADE,
    item_id         BIGINT       NOT NULL REFERENCES items (item_id) ON DELETE CASCADE,
    comment_created TIMESTAMP    NOT NULL
);

CREATE TABLE IF NOT EXISTS bookings
(
    booking_id     BIGINT GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    item_id        BIGINT      NOT NULL REFERENCES items (item_id) ON DELETE CASCADE,
    booker_id      BIGINT      NOT NULL REFERENCES users (user_id) ON DELETE CASCADE,
    booking_status VARCHAR(10) NOT NULL,
    booking_start  TIMESTAMP   NOT NULL,
    booking_end    TIMESTAMP   NOT NULL
);