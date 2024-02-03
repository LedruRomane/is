-- create if not exists tables
TRUNCATE company CASCADE;
TRUNCATE flight CASCADE;
TRUNCATE baggage CASCADE;

CREATE TABLE IF NOT EXISTS company (
    id VARCHAR(255) PRIMARY KEY
);

CREATE TABLE IF NOT EXISTS flight (
    id VARCHAR(255) PRIMARY KEY,
    livraisonencours BOOLEAN,
    nextnumerobagage INTEGER,
    company_id VARCHAR(255) REFERENCES company(id),
    pointlivraisonbagages VARCHAR(255)
);

CREATE TABLE IF NOT EXISTS baggage (
    numero INTEGER,
    delivre BOOLEAN,
    weight REAL,
    recupere BOOLEAN,
    passenger VARCHAR(255),
    flight_id VARCHAR(255) REFERENCES flight(id),
    PRIMARY KEY (numero, flight_id)
);

-- insert initial default roles
INSERT INTO company (id) VALUES ('company1'),('company2');

INSERT INTO flight (id, livraisonencours, nextnumerobagage, company_id, pointlivraisonbagages)
VALUES ('vol1', true, 22, 'company1', 'Paris'),
       ('vol2', false, 22, 'company1', 'Lyon'),
       ('vol3', false, 23, 'company2', 'Budapest'),
       ('vol4', true, 22, 'company2', 'London');

INSERT INTO baggage(numero, delivre, weight, recupere, passenger, flight_id)
VALUES (21, false, 2, false, 'Paul', 'vol1'),
       (21, true, 2, false, 'Jack', 'vol2'),
       (21, true, 2, true, 'Foo', 'vol3'),
       (22, true, 2, false, 'Doh', 'vol3'),
       (21, false, 2, false, 'John', 'vol4');
