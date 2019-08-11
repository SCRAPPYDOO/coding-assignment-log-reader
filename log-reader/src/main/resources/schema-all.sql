DROP TABLE log_entity IF EXISTS;

CREATE TABLE log_entity (
    id VARCHAR(20),
    type VARCHAR(20),
    host VARCHAR(20),
    started BIGINT,
    finished BIGINT,
    duration BIGINT,
    alert BOOLEAN DEFAULT FALSE
)