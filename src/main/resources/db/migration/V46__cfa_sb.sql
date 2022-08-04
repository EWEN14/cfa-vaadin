ALTER TABLE evenement
DROP
COLUMN created_at;

ALTER TABLE evenement
DROP
COLUMN updated_at;

ALTER TABLE evenement
    ADD created_at TIMESTAMP WITHOUT TIME ZONE;

ALTER TABLE evenement
    ADD updated_at TIMESTAMP WITHOUT TIME ZONE;