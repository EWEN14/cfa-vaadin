ALTER TABLE contrat
    ADD conv_avenant_remis_employeur BOOLEAN;

ALTER TABLE contrat
    ADD conv_avenant_remis_etudiant BOOLEAN;

ALTER TABLE contrat
    ADD conv_avenant_remis_tuteur BOOLEAN;

ALTER TABLE contrat
    ADD conv_originale_remis_employeur BOOLEAN;

ALTER TABLE contrat
    ADD conv_originale_remis_etudiant BOOLEAN;

ALTER TABLE contrat
    ADD conv_originale_remis_tuteur BOOLEAN;

ALTER TABLE contrat
    ADD date_convention_formation date;

ALTER TABLE contrat
    ADD date_depot_alfresco_conv_avn date;

ALTER TABLE contrat
    ADD date_depot_alfresco_cua_avn date;

ALTER TABLE contrat
    ADD date_mail_ou_rdv_signature_conv_avn date;

ALTER TABLE contrat
    ADD date_mail_ou_rdv_signature_cua_avn date;

ALTER TABLE contrat
    ADD motif_avn VARCHAR(255);

ALTER TABLE contrat
    ADD prime_avantage_nature VARCHAR(255);

ALTER TABLE formation
    ADD duree_hebdomadaire_travail INTEGER;

ALTER TABLE formation
    ADD heures_formation INTEGER;

ALTER TABLE formation
    ADD lieu_formation VARCHAR(255);

ALTER TABLE formation
    ADD semaines_entreprise INTEGER;

ALTER TABLE formation
    ADD semaines_formation INTEGER;

ALTER TABLE formation
    ADD type_emploi_exerce VARCHAR(255);

ALTER TABLE contrat
    DROP COLUMN cadre_admin_num_avenant;

ALTER TABLE contrat
    DROP COLUMN cadre_admin_num_enregistrement_contrat;

ALTER TABLE contrat
    DROP COLUMN cadre_admin_recu_le;

ALTER TABLE contrat
    DROP COLUMN code_rome_emploi_occupe;

ALTER TABLE contrat
    DROP COLUMN date_depot_alfresco_conv_avn_1;

ALTER TABLE contrat
    DROP COLUMN date_depot_alfresco_conv_avn_2;

ALTER TABLE contrat
    DROP COLUMN date_depot_alfresco_cua_avn_1;

ALTER TABLE contrat
    DROP COLUMN date_depot_alfresco_cua_avn_2;

ALTER TABLE contrat
    DROP COLUMN date_mail_ou_rdv_signature_conv_avn_1;

ALTER TABLE contrat
    DROP COLUMN date_mail_ou_rdv_signature_conv_avn_2;

ALTER TABLE contrat
    DROP COLUMN date_mail_ou_rdv_signature_cua_avn_1;

ALTER TABLE contrat
    DROP COLUMN date_mail_ou_rdv_signature_cua_avn_2;

ALTER TABLE contrat
    DROP COLUMN date_remise_originaux_avn_1;

ALTER TABLE contrat
    DROP COLUMN date_remise_originaux_avn_2;

ALTER TABLE contrat
    DROP COLUMN date_remise_originaux_cua_avn_2;

ALTER TABLE contrat
    DROP COLUMN duree_hebdomadaire_travail;

ALTER TABLE contrat
    DROP COLUMN emploi_occupe_salarie_etudiant;

ALTER TABLE contrat
    DROP COLUMN exemplaire_originaux_remis_alternant_ou_entreprise;

ALTER TABLE contrat
    DROP COLUMN heures_formation;

ALTER TABLE contrat
    DROP COLUMN lieu_formation;

ALTER TABLE contrat
    DROP COLUMN motif_avn_1;

ALTER TABLE contrat
    DROP COLUMN motif_avn_2;

ALTER TABLE contrat
    DROP COLUMN semaines_entreprise;

ALTER TABLE contrat
    DROP COLUMN semaines_formation;

UPDATE contrat
SET id_entreprise = '1'
WHERE id_entreprise IS NULL;
ALTER TABLE contrat
    ALTER COLUMN id_entreprise SET NOT NULL;

UPDATE contrat
SET id_etudiant = '200'
WHERE id_etudiant IS NULL;
ALTER TABLE contrat
    ALTER COLUMN id_etudiant SET NOT NULL;

UPDATE contrat
SET id_formation = '2'
WHERE id_formation IS NULL;
ALTER TABLE contrat
    ALTER COLUMN id_formation SET NOT NULL;

UPDATE contrat
SET id_tuteur = '130'
WHERE id_tuteur IS NULL;
ALTER TABLE contrat
    ALTER COLUMN id_tuteur SET NOT NULL;
