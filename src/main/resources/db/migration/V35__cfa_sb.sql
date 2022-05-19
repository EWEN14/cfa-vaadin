ALTER TABLE contrat
    ADD numero_avenant INTEGER;

UPDATE contrat
SET code_contrat = 'CONTRAT'
WHERE code_contrat IS NULL;
ALTER TABLE contrat
    ALTER COLUMN code_contrat SET NOT NULL;
