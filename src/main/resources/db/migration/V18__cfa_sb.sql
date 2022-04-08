CREATE TABLE formation
(
    id_formation            BIGINT NOT NULL,
    libelle_formation       VARCHAR(255),
    code_formation          VARCHAR(255),
    code_rome               VARCHAR(15),
    id_referent_pedagogique BIGINT,
    CONSTRAINT pk_formation PRIMARY KEY (id_formation)
);

ALTER TABLE referent_pedagogique
    ADD id_referent_pedagogique BIGINT;

ALTER TABLE referent_pedagogique
    DROP COLUMN id;

ALTER TABLE referent_pedagogique
    ADD CONSTRAINT pk_referent_pedagogique PRIMARY KEY (id_referent_pedagogique);

ALTER TABLE formation
    ADD CONSTRAINT FK_FORMATION_ON_ID_REFERENT_PEDAGOGIQUE FOREIGN KEY (id_referent_pedagogique) REFERENCES referent_pedagogique (id_referent_pedagogique);


