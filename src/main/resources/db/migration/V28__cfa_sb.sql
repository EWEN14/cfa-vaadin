ALTER TABLE contrat
    ADD date_mail_ou_rdv_signature_cua_avn_1 date;

ALTER TABLE contrat
    DROP COLUMN date_mail_ou_rdv_siganture_conv_avn_1;

ALTER TABLE contrat
    DROP COLUMN date_remise_originaux;

ALTER TABLE contrat
    DROP COLUMN code_postal_representant;

ALTER TABLE contrat
    DROP COLUMN created_at;

ALTER TABLE contrat
    DROP COLUMN telephone_representant;

ALTER TABLE contrat
    DROP COLUMN updated_at;

ALTER TABLE contrat
    ADD code_postal_representant INTEGER;

ALTER TABLE contrat
    ADD created_at TIMESTAMP WITHOUT TIME ZONE;

ALTER TABLE contrat
    ALTER COLUMN observations TYPE VARCHAR(15000) USING (observations::VARCHAR(15000));

ALTER TABLE contrat
    ADD telephone_representant INTEGER;

ALTER TABLE contrat
    ADD updated_at TIMESTAMP WITHOUT TIME ZONE;
