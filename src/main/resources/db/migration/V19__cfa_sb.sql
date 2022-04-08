CREATE TABLE tuteur_habilitation
(
    id_tuteur_habilitation BIGINT                      NOT NULL,
    statut_formation       VARCHAR(255)                NOT NULL,
    date_formation         date,
    id_formation           BIGINT,
    id_tuteur              BIGINT,
    created_at             TIMESTAMP WITHOUT TIME ZONE NOT NULL,
    updated_at             TIMESTAMP WITHOUT TIME ZONE NOT NULL,
    CONSTRAINT pk_tuteur_habilitation PRIMARY KEY (id_tuteur_habilitation)
);

ALTER TABLE formation
    ADD created_at TIMESTAMP WITHOUT TIME ZONE;

ALTER TABLE formation
    ADD updated_at TIMESTAMP WITHOUT TIME ZONE;

UPDATE formation
SET created_at = '2022-01-01 12:12:15.000000'
WHERE created_at IS NULL;
ALTER TABLE formation
    ALTER COLUMN created_at SET NOT NULL;

ALTER TABLE referent_pedagogique
    ADD created_at TIMESTAMP WITHOUT TIME ZONE;

ALTER TABLE referent_pedagogique
    ADD updated_at TIMESTAMP WITHOUT TIME ZONE;

UPDATE referent_pedagogique
SET created_at = '2022-01-01 12:12:15.000000'
WHERE created_at IS NULL;
ALTER TABLE referent_pedagogique
    ALTER COLUMN created_at SET NOT NULL;

UPDATE formation
SET updated_at = '2022-01-01 12:12:15.000000'
WHERE updated_at IS NULL;
ALTER TABLE formation
    ALTER COLUMN updated_at SET NOT NULL;

UPDATE referent_pedagogique
SET updated_at = '2022-01-01 12:12:15.000000'
WHERE updated_at IS NULL;
ALTER TABLE referent_pedagogique
    ALTER COLUMN updated_at SET NOT NULL;

ALTER TABLE tuteur_habilitation
    ADD CONSTRAINT FK_TUTEUR_HABILITATION_ON_ID_FORMATION FOREIGN KEY (id_formation) REFERENCES formation (id_formation);

ALTER TABLE tuteur_habilitation
    ADD CONSTRAINT FK_TUTEUR_HABILITATION_ON_ID_TUTEUR FOREIGN KEY (id_tuteur) REFERENCES tuteur (id_tuteur);
