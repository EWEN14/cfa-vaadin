ALTER TABLE entreprise
    DROP COLUMN nombre_salarie;

ALTER TABLE entreprise
    DROP COLUMN numero_cafat;

ALTER TABLE entreprise
    ADD nombre_salarie INTEGER;

ALTER TABLE entreprise
    ADD numero_cafat INTEGER;
