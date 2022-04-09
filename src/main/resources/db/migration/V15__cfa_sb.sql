ALTER TABLE etudiant
    ADD admis VARCHAR(255);

ALTER TABLE etudiant
    ADD adresse VARCHAR(255);

ALTER TABLE etudiant
    ADD age INTEGER;

ALTER TABLE etudiant
    ADD annee_obtention_dernier_diplome INTEGER;

ALTER TABLE etudiant
    ADD boite_postale VARCHAR(255);

ALTER TABLE etudiant
    ADD code_postal INTEGER;

ALTER TABLE etudiant
    ADD commune VARCHAR(255);

ALTER TABLE etudiant
    ADD dernier_diplome_obtenu_ou_en_cours VARCHAR(255);

ALTER TABLE etudiant
    ADD email VARCHAR(255);

ALTER TABLE etudiant
    ADD etablissement_de_provenance VARCHAR(255);

ALTER TABLE etudiant
    ADD lieu_naissance VARCHAR(255);

ALTER TABLE etudiant
    ADD nationalite VARCHAR(255);

ALTER TABLE etudiant
    ADD niveau_dernier_diplome INTEGER;

ALTER TABLE etudiant
    ADD numero_cafat INTEGER;

ALTER TABLE etudiant
    ADD observations VARCHAR(15000);

ALTER TABLE etudiant
    ADD obtention_diplome_mention VARCHAR(255);

ALTER TABLE etudiant
    ADD prise_en_charge_frais_inscription VARCHAR(255);

ALTER TABLE etudiant
    ADD sexe VARCHAR(255);

ALTER TABLE etudiant
    ADD situation_entreprise VARCHAR(255);

ALTER TABLE etudiant
    ADD situation_unc VARCHAR(255);

ALTER TABLE etudiant
    ADD telephone_1 INTEGER;

ALTER TABLE etudiant
    ADD telephone_2 INTEGER;

ALTER TABLE etudiant
    ADD travailleur_handicape BOOLEAN;

ALTER TABLE etudiant
    ADD veepap INTEGER;

UPDATE etudiant
SET admis = 'LC1'
WHERE admis IS NULL;
ALTER TABLE etudiant
    ALTER COLUMN admis SET NOT NULL;

ALTER TABLE entreprise
    ADD adr_phy_rue VARCHAR(255);

ALTER TABLE entreprise
    ADD adr_phys_commune VARCHAR(255);

ALTER TABLE entreprise
    ADD adr_phys_cp INTEGER;

ALTER TABLE entreprise
    ADD adr_post_commune VARCHAR(255);

ALTER TABLE entreprise
    ADD adr_post_rue_ou_bp VARCHAR(255);

ALTER TABLE entreprise
    ADD adr_postale_cp INTEGER;

ALTER TABLE entreprise
    ADD email_contact_cfa VARCHAR(255);

ALTER TABLE entreprise
    ADD fonction_contact_cfa VARCHAR(255);

ALTER TABLE entreprise
    ADD nom_contact_cfa VARCHAR(255);

ALTER TABLE entreprise
    ADD observations VARCHAR(15000);

ALTER TABLE entreprise
    ADD prenom_contact_cfa VARCHAR(255);

ALTER TABLE entreprise
    ADD telephone_contact_cfa VARCHAR(255);

UPDATE etudiant
SET dernier_diplome_obtenu_ou_en_cours = 'Bachelor'
WHERE dernier_diplome_obtenu_ou_en_cours IS NULL;
ALTER TABLE etudiant
    ALTER COLUMN dernier_diplome_obtenu_ou_en_cours SET NOT NULL;

UPDATE etudiant
SET email = 'testeur@etudiant.unc.nc'
WHERE email IS NULL;
ALTER TABLE etudiant
    ALTER COLUMN email SET NOT NULL;

ALTER TABLE tuteur
    ADD observations VARCHAR(15000);

ALTER TABLE tuteur
    ADD sexe VARCHAR(255);

UPDATE etudiant
SET telephone_1 = '123123'
WHERE telephone_1 IS NULL;
ALTER TABLE etudiant
    ALTER COLUMN telephone_1 SET NOT NULL;

UPDATE etudiant
SET telephone_2 = '445112'
WHERE telephone_2 IS NULL;
ALTER TABLE etudiant
    ALTER COLUMN telephone_2 SET NOT NULL;

ALTER TABLE entreprise
    DROP COLUMN boite_postale;

ALTER TABLE entreprise
    DROP COLUMN commune;

ALTER TABLE entreprise
    ALTER COLUMN forme_juridique DROP NOT NULL;

ALTER TABLE entreprise
    ALTER COLUMN raison_sociale DROP NOT NULL;

ALTER TABLE etudiant
    ALTER COLUMN situation_anne_precedente DROP NOT NULL;
