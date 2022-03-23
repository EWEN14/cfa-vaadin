ALTER TABLE entreprise
    ADD created_at TIMESTAMP WITHOUT TIME ZONE;

ALTER TABLE entreprise
    ADD id_entreprise BIGINT;

ALTER TABLE entreprise
    ADD updated_at TIMESTAMP WITHOUT TIME ZONE;

ALTER TABLE entreprise
    ALTER COLUMN created_at SET NOT NULL;

ALTER TABLE entreprise
    ALTER COLUMN updated_at SET NOT NULL;

ALTER TABLE etudiant
    ADD id_entreprise BIGINT;

ALTER TABLE etudiant
    ADD id_etudiant BIGINT;

ALTER TABLE log_enregistrement
    ADD id_log_enregistrement BIGINT;

ALTER TABLE entreprise
    DROP COLUMN id;

ALTER TABLE etudiant
    DROP COLUMN id;

ALTER TABLE log_enregistrement
    DROP COLUMN id;

ALTER TABLE entreprise
    ADD CONSTRAINT pk_entreprise PRIMARY KEY (id_entreprise);

ALTER TABLE etudiant
    ADD CONSTRAINT pk_etudiant PRIMARY KEY (id_etudiant);

ALTER TABLE log_enregistrement
    ADD CONSTRAINT pk_log_enregistrement PRIMARY KEY (id_log_enregistrement);

ALTER TABLE etudiant
    ADD CONSTRAINT FK_ETUDIANT_ON_ID_ENTREPRISE FOREIGN KEY (id_entreprise) REFERENCES entreprise (id_entreprise);
