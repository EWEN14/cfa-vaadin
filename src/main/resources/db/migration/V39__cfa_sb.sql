ALTER TABLE contrat
    ALTER COLUMN id_entreprise DROP NOT NULL;

ALTER TABLE contrat
    ALTER COLUMN id_etudiant DROP NOT NULL;

ALTER TABLE contrat
    ALTER COLUMN id_formation DROP NOT NULL;

ALTER TABLE contrat
    ALTER COLUMN id_tuteur DROP NOT NULL;
