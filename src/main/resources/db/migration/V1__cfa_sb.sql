CREATE SEQUENCE IF NOT EXISTS hibernate_sequence START WITH 1 INCREMENT BY 1;

CREATE TABLE application_user
(
    id              UUID                        NOT NULL,
    username        VARCHAR(255)                NOT NULL,
    nom             VARCHAR(255)                NOT NULL,
    prenom          VARCHAR(255)                NOT NULL,
    hashed_password VARCHAR(255)                NOT NULL,
    created_at      TIMESTAMP WITHOUT TIME ZONE NOT NULL,
    updated_at      TIMESTAMP WITHOUT TIME ZONE NOT NULL,
    CONSTRAINT pk_application_user PRIMARY KEY (id)
);

CREATE TABLE user_roles
(
    user_id UUID NOT NULL,
    roles   VARCHAR(255)
);

ALTER TABLE user_roles
    ADD CONSTRAINT fk_user_roles_on_user FOREIGN KEY (user_id) REFERENCES application_user (id);
