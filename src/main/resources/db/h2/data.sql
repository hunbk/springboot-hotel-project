-- 회원
INSERT INTO member VALUES (default, 'test1', 'test1234!@', '홍길동', 'hong', 'hong@email.com', '01012341234');

-- 방
INSERT INTO room VALUES (default, 101, 'SINGLE_OCEAN', 'single.jpg', 1, 1, 70000);
INSERT INTO room VALUES (default, 102, 'SINGLE_OCEAN', 'single.jpg', 1, 1, 70000);
INSERT INTO room VALUES (default, 201, 'DOUBLE_OCEAN', 'double.jpg', 2, 3, 100000);
INSERT INTO room VALUES (default, 202, 'DOUBLE_OCEAN', 'double.jpg', 2, 3, 100000);
INSERT INTO room VALUES (default, 301, 'TWIN_OCEAN', 'twin.jpg', 2, 3, 100000);
INSERT INTO room VALUES (default, 302, 'TWIN_OCEAN', 'twin.jpg', 2, 3, 100000);
INSERT INTO room VALUES (default, 401, 'FAMILY_OCEAN', 'family.jpg', 3, 4, 130000);
INSERT INTO room VALUES (default, 402, 'FAMILY_OCEAN', 'family.jpg', 3, 4, 130000);

-- 예약
INSERT INTO reservation VALUES (default, '2023-03-01', '2023-03-02', 2, current_timestamp(), 1, 1);
INSERT INTO reservation VALUES (default, '2023-03-02', '2023-03-03', 2, current_timestamp(), 1, 1);
INSERT INTO reservation VALUES (default, '2023-03-03', '2023-03-04', 2, current_timestamp(), 1, 1);
INSERT INTO reservation VALUES (default, '2023-03-04', '2023-03-05', 2, current_timestamp(), 1, 1);
INSERT INTO reservation VALUES (default, '2023-03-03', '2023-03-04', 2, current_timestamp(), 1, 2);
INSERT INTO reservation VALUES (default, '2023-03-04', '2023-03-05', 2, current_timestamp(), 1, 2);
INSERT INTO reservation VALUES (default, '2023-03-05', '2023-03-06', 2, current_timestamp(), 1, 2);
INSERT INTO reservation VALUES (default, '2023-03-06', '2023-03-07', 2, current_timestamp(), 1, 2);
INSERT INTO reservation VALUES (default, '2023-03-05', '2023-03-06', 2, current_timestamp(), 1, 3);
INSERT INTO reservation VALUES (default, '2023-03-06', '2023-03-07', 2, current_timestamp(), 1, 3);
INSERT INTO reservation VALUES (default, '2023-03-07', '2023-03-08', 2, current_timestamp(), 1, 3);
INSERT INTO reservation VALUES (default, '2023-03-08', '2023-03-09', 2, current_timestamp(), 1, 3);
INSERT INTO reservation VALUES (default, '2023-03-07', '2023-03-08', 2, current_timestamp(), 1, 4);
INSERT INTO reservation VALUES (default, '2023-03-08', '2023-03-09', 2, current_timestamp(), 1, 4);
INSERT INTO reservation VALUES (default, '2023-03-09', '2023-03-10', 2, current_timestamp(), 1, 4);
INSERT INTO reservation VALUES (default, '2023-03-10', '2023-03-11', 2, current_timestamp(), 1, 4);
INSERT INTO reservation VALUES (default, '2023-03-20', '2023-03-21', 2, current_timestamp(), 1, 1);
INSERT INTO reservation VALUES (default, '2023-03-20', '2023-03-21', 2, current_timestamp(), 1, 2);
INSERT INTO reservation VALUES (default, '2023-03-20', '2023-03-21', 2, current_timestamp(), 1, 3);
INSERT INTO reservation VALUES (default, '2023-03-20', '2023-03-21', 2, current_timestamp(), 1, 4);
INSERT INTO reservation VALUES (default, '2023-03-20', '2023-03-21', 2, current_timestamp(), 1, 5);
INSERT INTO reservation VALUES (default, '2023-03-20', '2023-03-21', 2, current_timestamp(), 1, 6);
INSERT INTO reservation VALUES (default, '2023-03-20', '2023-03-21', 2, current_timestamp(), 1, 7);
INSERT INTO reservation VALUES (default, '2023-03-20', '2023-03-21', 2, current_timestamp(), 1, 8);

-- 주문
INSERT INTO orders VALUES (default, 1, 1, 100000, current_timestamp());
INSERT INTO orders VALUES (default, 1, 2, 100000, current_timestamp());
INSERT INTO orders VALUES (default, 1, 3, 100000, current_timestamp());
INSERT INTO orders VALUES (default, 1, 4, 100000, current_timestamp());
INSERT INTO orders VALUES (default, 1, 5, 100000, current_timestamp());
INSERT INTO orders VALUES (default, 1, 6, 100000, current_timestamp());
INSERT INTO orders VALUES (default, 1, 7, 100000, current_timestamp());
INSERT INTO orders VALUES (default, 1, 8, 100000, current_timestamp());
INSERT INTO orders VALUES (default, 1, 9, 100000, current_timestamp());
INSERT INTO orders VALUES (default, 1, 10, 100000, current_timestamp());
INSERT INTO orders VALUES (default, 1, 11, 100000, current_timestamp());
INSERT INTO orders VALUES (default, 1, 12, 100000, current_timestamp());
INSERT INTO orders VALUES (default, 1, 13, 100000, current_timestamp());
INSERT INTO orders VALUES (default, 1, 14, 100000, current_timestamp());
INSERT INTO orders VALUES (default, 1, 15, 100000, current_timestamp());
INSERT INTO orders VALUES (default, 1, 16, 100000, current_timestamp());
