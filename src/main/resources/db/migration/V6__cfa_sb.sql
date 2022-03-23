CREATE TABLE entreprise
(
    id       BIGINT       NOT NULL,
    enseigne VARCHAR(255) NOT NULL,
    CONSTRAINT pk_entreprise PRIMARY KEY (id)
);

ALTER TABLE log_enregistrement
    ADD created_at TIMESTAMP WITHOUT TIME ZONE;

UPDATE log_enregistrement
SET created_at = '2022-01-14 14:01:22.123456'
WHERE created_at IS NULL;
ALTER TABLE log_enregistrement
    ALTER COLUMN created_at SET NOT NULL;

ALTER TABLE etudiant
    ADD situation_anne_precedente VARCHAR(255);

UPDATE etudiant
SET situation_anne_precedente = 'ÉTUDIANT'
WHERE situation_anne_precedente IS NULL;
ALTER TABLE etudiant
    ALTER COLUMN situation_anne_precedente SET NOT NULL;
