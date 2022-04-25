ALTER TABLE etudiant
    ADD id_tuteur BIGINT;

ALTER TABLE etudiant
    ADD CONSTRAINT FK_ETUDIANT_ON_ID_TUTEUR FOREIGN KEY (id_tuteur) REFERENCES tuteur (id_tuteur);
