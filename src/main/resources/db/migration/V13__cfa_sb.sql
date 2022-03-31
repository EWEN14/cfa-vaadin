ALTER TABLE tuteur
    ADD certificat_travail_fourni BOOLEAN;

ALTER TABLE tuteur
    ADD cv_fourni BOOLEAN;

ALTER TABLE tuteur
    ADD diplome_fourni BOOLEAN;

ALTER TABLE tuteur
    ADD created_at TIMESTAMP WITHOUT TIME ZONE;

UPDATE tuteur
SET created_at = '2022-03-23 13:34:35.793796'
WHERE created_at IS NULL;
ALTER TABLE tuteur
    ALTER COLUMN created_at SET NOT NULL;

ALTER TABLE tuteur
    ADD updated_at TIMESTAMP WITHOUT TIME ZONE;

UPDATE tuteur
SET updated_at = '2022-03-23 13:34:35.793796'
WHERE updated_at IS NULL;
ALTER TABLE tuteur
    ALTER COLUMN updated_at SET NOT NULL;

ALTER TABLE tuteur
    DROP COLUMN pj1;

ALTER TABLE tuteur
    DROP COLUMN pj2;

ALTER TABLE tuteur
    DROP COLUMN niveau_diplome;

ALTER TABLE tuteur
    ALTER COLUMN date_naissance DROP NOT NULL;

UPDATE tuteur
SET email = 'testeur@gmail.com'
WHERE email IS NULL;
ALTER TABLE tuteur
    ALTER COLUMN email SET NOT NULL;

ALTER TABLE tuteur
    ADD niveau_diplome INTEGER;

ALTER TABLE tuteur
    ALTER COLUMN niveau_diplome DROP NOT NULL;

UPDATE tuteur
SET telephone_1 = '123123'
WHERE telephone_1 IS NULL;
ALTER TABLE tuteur
    ALTER COLUMN telephone_1 SET NOT NULL;
