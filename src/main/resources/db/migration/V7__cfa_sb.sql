ALTER TABLE entreprise
    ADD activite_entreprise VARCHAR(255);

ALTER TABLE entreprise
    ADD boite_postale VARCHAR(255);

ALTER TABLE entreprise
    ADD code_naf VARCHAR(255);

ALTER TABLE entreprise
    ADD commune VARCHAR(255);

ALTER TABLE entreprise
    ADD convention_collective VARCHAR(255);

ALTER TABLE entreprise
    ADD fonction_representant_employeur VARCHAR(255);

ALTER TABLE entreprise
    ADD forme_juridique VARCHAR(255);

ALTER TABLE entreprise
    ADD nom_representant_employeur VARCHAR(255);

ALTER TABLE entreprise
    ADD nombre_salarie BIGINT;

ALTER TABLE entreprise
    ADD numero_cafat BIGINT;

ALTER TABLE entreprise
    ADD numero_ridet VARCHAR(255);

ALTER TABLE entreprise
    ADD prenom_representant_employeur VARCHAR(255);

ALTER TABLE entreprise
    ADD raison_sociale VARCHAR(255);

ALTER TABLE entreprise
    ADD statut_actif_entreprise VARCHAR(255);

UPDATE entreprise
SET forme_juridique = 'SARL'
WHERE forme_juridique IS NULL;
ALTER TABLE entreprise
    ALTER COLUMN forme_juridique SET NOT NULL;

UPDATE entreprise
SET raison_sociale = 'SARL machin'
WHERE raison_sociale IS NULL;
ALTER TABLE entreprise
    ALTER COLUMN raison_sociale SET NOT NULL;

UPDATE entreprise
SET statut_actif_entreprise = 'ENTREPRISE ACTIVE'
WHERE statut_actif_entreprise IS NULL;
ALTER TABLE entreprise
    ALTER COLUMN statut_actif_entreprise SET NOT NULL;
