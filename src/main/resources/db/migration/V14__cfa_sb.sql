CREATE TABLE referent_pedagogique
(
    id        BIGINT       NOT NULL,
    nom       VARCHAR(255) NOT NULL,
    prenom    VARCHAR(255) NOT NULL,
    email     VARCHAR(255) NOT NULL,
    telephone INTEGER      NOT NULL,
    civilite  VARCHAR(255) NOT NULL,
    CONSTRAINT pk_referent_pedagogique PRIMARY KEY (id)
);

ALTER TABLE tuteur
    ADD civilite VARCHAR(255);