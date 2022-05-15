ALTER TABLE formation
    ADD niveau_certification_pro INTEGER;

ALTER TABLE contrat
    DROP COLUMN niveau_certification_pro;
