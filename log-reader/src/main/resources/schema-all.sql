DROP TABLE logs IF EXISTS;

CREATE TABLE logs (
    id VARCHAR(20),
    state VARCHAR(20),
    type VARCHAR(20),
    host VARCHAR(20),
    timestamp TIMESTAMP
);