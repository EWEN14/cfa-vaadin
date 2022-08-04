ALTER TABLE referent_cfa
DROP
COLUMN created_at;

ALTER TABLE referent_cfa
DROP
COLUMN updated_at;

ALTER TABLE referent_cfa
    ADD created_at TIMESTAMP WITHOUT TIME ZONE;

ALTER TABLE referent_cfa
    ADD updated_at TIMESTAMP WITHOUT TIME ZONE;