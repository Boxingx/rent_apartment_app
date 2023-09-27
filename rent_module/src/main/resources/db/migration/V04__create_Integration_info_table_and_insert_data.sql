CREATE TABLE IF NOT EXISTS integration_info (
    id int8 PRIMARY KEY NOT NULL,
    service_id varchar,
    service_name varchar,
    service_token varchar,
    service_path varchar
);

CREATE SEQUENCE integration_info_sequence start 2 increment 1;

INSERT INTO integration_info
VALUES
(1, 'GEO-01', 'opencagedata', 'Mzk5YzExY2NhMjA2NDAwOGJhNDJjMzAxYWY5OGM5MDY=', 'https://api.opencagedata.com/geocode/v1/json?q=%s+%s&no_annotations=1&key=%s')
