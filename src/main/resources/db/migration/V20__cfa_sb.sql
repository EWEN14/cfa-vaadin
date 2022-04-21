CREATE TABLE contrat
(
    id_contrat                            BIGINT NOT NULL,
    type_contrat                          VARCHAR(255),
    nom_representant_legal                VARCHAR(255),
    prenom_representant_legal             VARCHAR(255),
    relation_avec_salarie                 VARCHAR(255),
    adresse_representant                  VARCHAR(255),
    code_postal_representant              VARCHAR(255),
    commune_representant                  VARCHAR(255),
    telephone_representant                VARCHAR(255),
    email_representant                    VARCHAR(255),
    debut_contrat                         date,
    fin_contrat                           date,
    duree_periode_essai                   INTEGER,
    intitule_certification_pro            VARCHAR(255),
    niveau_certification_pro              INTEGER,
    formation_assuree                     VARCHAR(255),
    numero_convention_tripartie           VARCHAR(255),
    date_convention_tripartie             date,
    semaines_entreprise                   INTEGER,
    heures_formation                      INTEGER,
    semaines_formation                    INTEGER,
    lieu_formation                        VARCHAR(255),
    duree_hebdomadaire_travail            INTEGER,
    date_rupture                          date,
    motif_rupture                         VARCHAR(255),
    date_reception_decua                  date,
    date_envoi_rp_decua                   date,
    date_retour_rp_decua                  date,
    date_envoi_email_cua_convention       date,
    date_depot_alfresco_cua_avn_1         date,
    date_remise_originaux                 date,
    motif_avn_2                           VARCHAR(255),
    date_mail_ou_rdv_signature_cua_avn_2  date,
    date_depot_alfresco_cua_avn_2         date,
    date_mail_ou_rdv_signature_conv_avn_2 date,
    date_depot_alfresco_conv_avn_2        date,
    date_remise_originaux_avn_2           date,
    formation_lea                         date,
    observations                          VARCHAR(2000),
    created_at                            date,
    updated_at                            date,
    code_contrat                          VARCHAR(255),
    date_depot_alfresco_cua_conv_signe    date,
    date_reception_originaux_convention   date,
    motif_avn_1                           VARCHAR(255),
    date_mail_ou_rdv_siganture_conv_avn_1 date,
    date_mail_ou_rdv_signature_conv_avn_1 date,
    date_depot_alfresco_conv_avn_1        date,
    date_remise_originaux_avn_1           date,
    id_entreprise                         BIGINT,
    id_etudiant                           BIGINT,
    id_formation                          BIGINT,
    id_tuteur                             BIGINT,
    CONSTRAINT pk_contrat PRIMARY KEY (id_contrat)
);

ALTER TABLE contrat
    ADD CONSTRAINT FK_CONTRAT_ON_ID_ENTREPRISE FOREIGN KEY (id_entreprise) REFERENCES entreprise (id_entreprise);

ALTER TABLE contrat
    ADD CONSTRAINT FK_CONTRAT_ON_ID_ETUDIANT FOREIGN KEY (id_etudiant) REFERENCES etudiant (id_etudiant);

ALTER TABLE contrat
    ADD CONSTRAINT FK_CONTRAT_ON_ID_FORMATION FOREIGN KEY (id_formation) REFERENCES formation (id_formation);

ALTER TABLE contrat
    ADD CONSTRAINT FK_CONTRAT_ON_ID_TUTEUR FOREIGN KEY (id_tuteur) REFERENCES tuteur (id_tuteur);