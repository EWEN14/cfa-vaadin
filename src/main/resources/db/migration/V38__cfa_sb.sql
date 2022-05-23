ALTER TABLE contrat
    ADD id_contrat_parent BIGINT;

ALTER TABLE contrat
    ADD CONSTRAINT FK_CONTRAT_ON_ID_CONTRAT_PARENT FOREIGN KEY (id_contrat_parent) REFERENCES contrat (id_contrat);
