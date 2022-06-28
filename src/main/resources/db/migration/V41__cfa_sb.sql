ALTER TABLE evenement
    ADD libelle VARCHAR(255);

ALTER TABLE evenement
DROP
COLUMN date_debut;

ALTER TABLE evenement
DROP
COLUMN date_fin;

ALTER TABLE evenement
    ADD date_debut date NOT NULL;

ALTER TABLE evenement
    ADD date_fin date NOT NULL;