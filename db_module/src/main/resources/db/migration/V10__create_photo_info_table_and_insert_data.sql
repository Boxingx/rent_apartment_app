create sequence photo_info_sequence;

CREATE TABLE IF NOT EXISTS photo_info (
    id int8 PRIMARY KEY NOT NULL,
    image_data bytea,
    apartment_id int8 REFERENCES apartment_info(id)
)