ALTER TABLE formation
    ADD observations VARCHAR(15000);

ALTER TABLE formation
    ALTER COLUMN id_referent_pedagogique SET NOT NULL;
