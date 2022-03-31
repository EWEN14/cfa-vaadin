ALTER TABLE tuteur
    ADD annee_experience_professionnelle VARCHAR(255);

ALTER TABLE tuteur
DROP
COLUMN experience_professionnelle;

ALTER TABLE tuteur
DROP
COLUMN niveau_diplome;

ALTER TABLE tuteur
DROP
COLUMN telephone_2;

ALTER TABLE tuteur
    ADD niveau_diplome BIGINT NOT NULL;

ALTER TABLE tuteur
    ADD telephone_2 INTEGER;