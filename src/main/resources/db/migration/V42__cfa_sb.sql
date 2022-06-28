CREATE TABLE formation_evenements
(
    evenements_id BIGINT NOT NULL,
    formation_id  BIGINT NOT NULL,
    CONSTRAINT pk_formation_evenements PRIMARY KEY (evenements_id, formation_id)
);

ALTER TABLE formation_evenements
    ADD CONSTRAINT fk_foreve_on_evenement FOREIGN KEY (evenements_id) REFERENCES evenement (id_evenement);

ALTER TABLE formation_evenements
    ADD CONSTRAINT fk_foreve_on_formation FOREIGN KEY (formation_id) REFERENCES formation (id_formation);

ALTER TABLE evenement_formations
DROP
CONSTRAINT fk_evefor_on_evenement;

ALTER TABLE evenement_formations
DROP
CONSTRAINT fk_evefor_on_formation;

DROP TABLE evenement_formations CASCADE;