CREATE TABLE log_enregistrement
(
    id        BIGINT       NOT NULL,
    log_trace TEXT         NOT NULL,
    type_crud VARCHAR(255) NOT NULL,
    CONSTRAINT pk_log_enregistrement PRIMARY KEY (id)
);
