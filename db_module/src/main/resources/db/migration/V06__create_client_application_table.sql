CREATE SEQUENCE client_application_sequence start 2 increment 1;

CREATE TABLE IF NOT EXISTS client_application (
    id int8 PRIMARY KEY NOT NULL,
    nick_name varchar(100),
    login_mail varchar(200),
    password varchar(100),
    commercial_status boolean default false,
    booking_count int4,
    parent_city varchar,
    user_token varchar
);

INSERT INTO client_application
VALUES
(1, 'testUser', 'fire301095@mail.ru', '321', false, null, 'Омск', 'testToken|2030-10-07T12:49:43.641604600')


