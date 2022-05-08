ALTER TABLE contrat
    ADD cadre_admin_num_avenant VARCHAR(255);

ALTER TABLE contrat
    ADD cadre_admin_num_enregistrement_contrat VARCHAR(255);

ALTER TABLE contrat
    ADD cadre_admin_recu_le date;

ALTER TABLE contrat
    ADD code_rome_emploi_occupe VARCHAR(15);

ALTER TABLE contrat
    ADD date_delivrance_derogation_age date;

ALTER TABLE contrat
    ADD date_remise_originaux_cua_avn_2 date;

ALTER TABLE contrat
    ADD derogation_age BOOLEAN;

ALTER TABLE contrat
    ADD emploi_occupe_salarie_etudiant VARCHAR(255);

ALTER TABLE contrat
    ADD exemplaire_originaux_remis_alternant_ou_entreprise VARCHAR(255);

ALTER TABLE contrat
    ADD numero_convention_formation VARCHAR(255);

ALTER TABLE tuteur_habilitation
    ADD date_habilitation date;

ALTER TABLE tuteur_habilitation
    ADD modalite_formation VARCHAR(255);

ALTER TABLE etudiant
    ADD dernier_emploi_occupe VARCHAR(255);

ALTER TABLE etudiant
    ADD numero_etudiant INTEGER;

ALTER TABLE entreprise
    ADD email_entreprise VARCHAR(255);

ALTER TABLE entreprise
    ADD telephone_entreprise INTEGER;

ALTER TABLE contrat
    DROP COLUMN date_convention_tripartie;

ALTER TABLE contrat
    DROP COLUMN intitule_certification_pro;

ALTER TABLE contrat
    DROP COLUMN numero_convention_tripartie;
