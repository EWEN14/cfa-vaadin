CREATE TABLE entretien_collectif
(
    id              BIGINT NOT NULL,
    date            date   NOT NULL,
    observations    VARCHAR(15000),
    created_at      TIMESTAMP WITHOUT TIME ZONE,
    updated_at      TIMESTAMP WITHOUT TIME ZONE,
    id_formation    BIGINT NOT NULL,
    id_referent_cfa BIGINT NOT NULL,
    CONSTRAINT pk_entretiencollectif PRIMARY KEY (id)
);

ALTER TABLE entretien_collectif
    ADD CONSTRAINT FK_ENTRETIENCOLLECTIF_ON_ID_FORMATION FOREIGN KEY (id_formation) REFERENCES formation (id_formation);

ALTER TABLE entretien_collectif
    ADD CONSTRAINT FK_ENTRETIENCOLLECTIF_ON_ID_REFERENT_CFA FOREIGN KEY (id_referent_cfa) REFERENCES referent_cfa (id_referent_cfa);