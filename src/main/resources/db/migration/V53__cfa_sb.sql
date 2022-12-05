ALTER TABLE contrat ALTER COLUMN statut_actif DROP DEFAULT;

ALTER TABLE contrat ALTER COLUMN missions_alternant DROP DEFAULT;

ALTER TABLE etudiant ALTER COLUMN statut_actif DROP DEFAULT;

ALTER TABLE formation ALTER COLUMN statut_actif DROP DEFAULT;

ALTER TABLE formation ALTER COLUMN heures_projet_universitaire DROP DEFAULT;

ALTER TABLE tuteur ALTER COLUMN statut_actif DROP DEFAULT;
