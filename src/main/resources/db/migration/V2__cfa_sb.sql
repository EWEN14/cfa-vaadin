CREATE TABLE etudiant
(
    id             BIGINT                      NOT NULL,
    nom            VARCHAR(255)                NOT NULL,
    prenom         VARCHAR(255)                NOT NULL,
    civilite       VARCHAR(255)                NOT NULL,
    date_naissance date                        NOT NULL,
    created_at     TIMESTAMP WITHOUT TIME ZONE NOT NULL,
    updated_at     TIMESTAMP WITHOUT TIME ZONE NOT NULL,
    CONSTRAINT pk_etudiant PRIMARY KEY (id)
);
