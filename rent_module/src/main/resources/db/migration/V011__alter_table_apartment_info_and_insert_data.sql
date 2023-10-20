ALTER TABLE apartment_info
ADD COLUMN owner int8 REFERENCES client_application(id);

UPDATE apartment_info
SET owner = null
WHERE id BETWEEN 1 AND 15;