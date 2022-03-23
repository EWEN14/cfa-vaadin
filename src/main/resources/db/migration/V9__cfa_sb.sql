CREATE TABLE tuteur
(
    id_tuteur                  BIGINT       NOT NULL,
    nom                        VARCHAR(255) NOT NULL,
    prenom                     VARCHAR(255) NOT NULL,
    date_naissance             date         NOT NULL,
    email                      VARCHAR(255),
    casier_judiciaire_fourni   BOOLEAN,
    diplome_eleve_obtenu       VARCHAR(255),
    poste_occupe               VARCHAR(255),
    experience_professionnelle VARCHAR(255),
    niveau_diplome             VARCHAR(255) NOT NULL,
    pj1                        VARCHAR(255) NOT NULL,
    pj2                        VARCHAR(255) NOT NULL,
    telephone_1                INTEGER,
    telephone_2                VARCHAR(255),
    id_entreprise              BIGINT,
    CONSTRAINT pk_tuteur PRIMARY KEY (id_tuteur)
);

ALTER TABLE tuteur
    ADD CONSTRAINT FK_TUTEUR_ON_ID_ENTREPRISE FOREIGN KEY (id_entreprise) REFERENCES entreprise (id_entreprise);

/*ALTER TABLE etudiant
DROP
COLUMN test;*/