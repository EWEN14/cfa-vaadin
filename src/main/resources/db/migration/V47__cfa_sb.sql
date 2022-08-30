CREATE TABLE entretien_individuelle
(
    id              BIGINT NOT NULL,
    date            date   NOT NULL,
    observations    VARCHAR(15000),
    created_at      TIMESTAMP WITHOUT TIME ZONE,
    updated_at      TIMESTAMP WITHOUT TIME ZONE,
    id_etudiant     BIGINT NOT NULL,
    id_referent_cfa BIGINT NOT NULL,
    CONSTRAINT pk_entretien_individuelle PRIMARY KEY (id)
);

ALTER TABLE entretien_individuelle
    ADD CONSTRAINT FK_ENTRETIEN_INDIVIDUELLE_ON_ID_ETUDIANT FOREIGN KEY (id_etudiant) REFERENCES etudiant (id_etudiant);

ALTER TABLE entretien_individuelle
    ADD CONSTRAINT FK_ENTRETIEN_INDIVIDUELLE_ON_ID_REFERENT_CFA FOREIGN KEY (id_referent_cfa) REFERENCES referent_cfa (id_referent_cfa);