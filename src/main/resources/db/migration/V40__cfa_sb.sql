CREATE TABLE evenement
(
    id_evenement BIGINT NOT NULL,
    description  VARCHAR(255),
    date_debut   TIMESTAMP WITHOUT TIME ZONE NOT NULL,
    date_fin     TIMESTAMP WITHOUT TIME ZONE NOT NULL,
    CONSTRAINT pk_evenement PRIMARY KEY (id_evenement)
);

CREATE TABLE evenement_formations
(
    id_evenement BIGINT NOT NULL,
    id_formation BIGINT NOT NULL
);

ALTER TABLE evenement_formations
    ADD CONSTRAINT fk_evefor_on_evenement FOREIGN KEY (id_evenement) REFERENCES evenement (id_evenement);

ALTER TABLE evenement_formations
    ADD CONSTRAINT fk_evefor_on_formation FOREIGN KEY (id_formation) REFERENCES formation (id_formation);