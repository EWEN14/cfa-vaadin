ALTER TABLE log_enregistrement
    ADD description_log TEXT;

ALTER TABLE log_enregistrement
    ALTER COLUMN description_log SET NOT NULL;

ALTER TABLE log_enregistrement
    DROP COLUMN log_trace;
