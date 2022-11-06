ALTER TABLE formation
    ADD statut_actif varchar(40) NOT NULL default 'ACTIF';

ALTER TABLE etudiant
    ADD statut_actif varchar(40) NOT NULL default 'ACTIF';

ALTER TABLE etudiant
    ADD nom_jeune_fille varchar(255);

ALTER TABLE etudiant
    ADD suivre_etudiant BOOLEAN;

ALTER TABLE tuteur
    ADD statut_actif varchar(40) NOT NULL default 'ACTIF';

ALTER TABLE contrat
    ADD statut_actif varchar(40) NOT NULL default 'ACTIF';

ALTER TABLE contrat
    ADD salaire_negocie INTEGER;

ALTER TABLE contrat
    ADD missions_alternant varchar(15000) NOT NULL default 'aucune';

CREATE TABLE numero_convention_formation
(
    id_numero_convention_formation BIGINT NOT NULL,
    numero_convention INTEGER      NOT NULL,
    CONSTRAINT pk_numero_convention_formation PRIMARY KEY (id_numero_convention_formation)
);

INSERT INTO numero_convention_formation VALUES (1,1);
