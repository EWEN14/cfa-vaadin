CREATE TABLE evenement_formations
(
    evenement_id  BIGINT NOT NULL,
    formations_id BIGINT NOT NULL,
    CONSTRAINT pk_evenement_formations PRIMARY KEY (evenement_id, formations_id)
);

ALTER TABLE evenement_formations
    ADD CONSTRAINT fk_evefor_on_evenement FOREIGN KEY (evenement_id) REFERENCES evenement (id_evenement);

ALTER TABLE evenement_formations
    ADD CONSTRAINT fk_evefor_on_formation FOREIGN KEY (formations_id) REFERENCES formation (id_formation);

ALTER TABLE formation_evenements
DROP
CONSTRAINT fk_foreve_on_evenement;

ALTER TABLE formation_evenements
DROP
CONSTRAINT fk_foreve_on_formation;

DROP TABLE formation_evenements CASCADE;