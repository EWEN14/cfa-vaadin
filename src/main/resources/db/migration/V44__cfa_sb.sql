CREATE TABLE referent_cfa
(
    id_referent_cfa BIGINT       NOT NULL,
    prenom          VARCHAR(255) NOT NULL,
    nom             VARCHAR(255) NOT NULL,
    email           VARCHAR(255) NOT NULL,
    civilite        VARCHAR(255) NOT NULL,
    telephone       INTEGER      NOT NULL,
    created_at      date,
    updated_at      date,
    CONSTRAINT pk_referent_cfa PRIMARY KEY (id_referent_cfa)
);

ALTER TABLE evenement
    ADD created_at date;

ALTER TABLE evenement
    ADD updated_at date;

ALTER TABLE formation
    ALTER COLUMN code_formation SET NOT NULL;

ALTER TABLE contrat
    ALTER COLUMN debut_contrat SET NOT NULL;

ALTER TABLE contrat
    ALTER COLUMN fin_contrat SET NOT NULL;

ALTER TABLE evenement
    ALTER COLUMN libelle SET NOT NULL;

ALTER TABLE formation
    ALTER COLUMN libelle_formation SET NOT NULL;

ALTER TABLE entreprise
    ALTER COLUMN numero_ridet SET NOT NULL;

ALTER TABLE entreprise
    ALTER COLUMN raison_sociale SET NOT NULL;