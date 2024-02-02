-- create if not exists tables
TRUNCATE company CASCADE;
TRUNCATE vol CASCADE;
TRUNCATE bagage CASCADE;

CREATE TABLE IF NOT EXISTS company (
    id VARCHAR(255) PRIMARY KEY
);

CREATE TABLE IF NOT EXISTS vol (
    id VARCHAR(255) PRIMARY KEY,
    livraisonencours BOOLEAN,
    nextnumerobagage INTEGER,
    company_id VARCHAR(255) REFERENCES company(id),
    pointlivraisonbagages VARCHAR(255)
);

CREATE TABLE IF NOT EXISTS bagage (
    numero INTEGER,
    delivre BOOLEAN,
    poids REAL,
    recupere BOOLEAN,
    passager VARCHAR(255),
    vol_id VARCHAR(255) REFERENCES vol(id),
    PRIMARY KEY (numero, vol_id)
);

-- insert initial default roles
INSERT INTO company (id) VALUES ('company1'),('company2');

INSERT INTO vol (id, livraisonencours, nextnumerobagage, company_id, pointlivraisonbagages)
VALUES ('vol1', true, 22, 'company1', 'Paris'),
       ('vol2', false, 22, 'company1', 'Lyon'),
       ('vol3', false, 23, 'company2', 'Budapest'),
       ('vol4', true, 22, 'company2', 'London');

INSERT INTO bagage(numero, delivre, poids, recupere, passager, vol_id)
VALUES (21, false, 2, false, 'Paul', 'vol1'),
       (21, true, 2, false, 'Jack', 'vol2'),
       (21, true, 2, true, 'Foo', 'vol3'),
       (22, true, 2, false, 'Doh', 'vol3'),
       (21, false, 2, false, 'John', 'vol4');
