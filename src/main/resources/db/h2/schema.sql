DROP TABLE IF EXISTS room CASCADE;
DROP TABLE IF EXISTS board CASCADE;
DROP TABLE IF EXISTS member CASCADE;
DROP TABLE IF EXISTS reservation CASCADE;

CREATE TABLE member (
    member_id  BIGINT GENERATED BY DEFAULT AS IDENTITY,
    login_id   VARCHAR(20) NOT NULL,
    password   VARCHAR(30) NOT NULL,
    name       VARCHAR(10) NOT NULL,
    nickname   VARCHAR(12) NOT NULL,
    email      VARCHAR(50) NOT NULL,
    cell_phone VARCHAR(11) NOT NULL,
    PRIMARY KEY (member_id)
);

CREATE TABLE board (
    board_id      BIGINT GENERATED BY DEFAULT AS IDENTITY,
    title         VARCHAR(50) NOT NULL,
    content       TEXT        NOT NULL,
    created_date  TIMESTAMP,
    modified_date TIMESTAMP,
    member_id     BIGINT      NOT NULL,
    PRIMARY KEY (board_id)
);

CREATE TABLE reservation (
    reservation_id BIGINT GENERATED BY DEFAULT AS IDENTITY,
    check_in       DATE    NOT NULL,
    check_out      DATE    NOT NULL,
    personnel      INTEGER NOT NULL,
    created_date   TIMESTAMP,
    member_id      BIGINT,
    room_id        BIGINT,
    PRIMARY KEY (reservation_id)
);

CREATE TABLE room (
    room_id           BIGINT GENERATED BY DEFAULT AS IDENTITY,
    room_no           INTEGER     NOT NULL,
    room_type         VARCHAR(20) NOT NULL,
    photo_url         VARCHAR(50) NOT NULL,
    default_personnel INTEGER     NOT NULL,
    max_personnel     INTEGER     NOT NULL,
    price             INTEGER     NOT NULL,
    PRIMARY KEY (room_id)
);

CREATE TABLE orders (
    order_id       BIGINT GENERATED BY DEFAULT AS IDENTITY,
    member_id      BIGINT  NOT NULL,
    reservation_id BIGINT  NOT NULL,
    order_price    INTEGER NOT NULL,
    created_date   TIMESTAMP,
    PRIMARY KEY (order_id)
);

ALTER TABLE member
    ADD CONSTRAINT uk_member_email UNIQUE (email);

ALTER TABLE member
    ADD CONSTRAINT uk_member_loginId UNIQUE (login_id);

ALTER TABLE member
    ADD CONSTRAINT uk_member_nickname UNIQUE (nickname);

ALTER TABLE board
    ADD CONSTRAINT fk_board_member FOREIGN KEY (member_id) REFERENCES member;

ALTER TABLE reservation
    ADD CONSTRAINT fk_reservation_member FOREIGN KEY (member_id) REFERENCES member;

ALTER TABLE reservation
    ADD CONSTRAINT fk_reservation_room FOREIGN KEY (room_id) REFERENCES room;

ALTER TABLE orders
    ADD CONSTRAINT fk_orders_member FOREIGN KEY (member_id) REFERENCES member;

ALTER TABLE orders
    ADD CONSTRAINT fk_orders_reservation FOREIGN KEY (reservation_id) REFERENCES reservation;
