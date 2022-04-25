ALTER TABLE etudiant
    ADD id_formation BIGINT;

ALTER TABLE etudiant
    ADD id_referent_pedagogique BIGINT;

ALTER TABLE etudiant
    ADD CONSTRAINT FK_ETUDIANT_ON_ID_FORMATION FOREIGN KEY (id_formation) REFERENCES formation (id_formation);

ALTER TABLE etudiant
    ADD CONSTRAINT FK_ETUDIANT_ON_ID_REFERENT_PEDAGOGIQUE FOREIGN KEY (id_referent_pedagogique) REFERENCES referent_pedagogique (id_referent_pedagogique);
