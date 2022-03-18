ALTER TABLE log_enregistrement
    ALTER COLUMN description_log TYPE VARCHAR(15000) USING (description_log::VARCHAR(15000));
