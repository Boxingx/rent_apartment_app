CREATE TABLE IF NOT EXISTS promo_code_info (
    promo_id int8 PRIMARY KEY NOT NULL,
    promo_code varchar(32),
    status varchar(32),
    discount int8,
    discount_type varchar
    );

CREATE SEQUENCE promo_code_info_sequence start 2 increment 1;

INSERT INTO promo_code_info
values (1, 'slf4g', 'true', 5, 'Процент'),
       (2, 'winter', 'true', 7, 'Процент');
