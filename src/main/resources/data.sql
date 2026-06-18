INSERT INTO test_counters (name, counter) VALUES ('test1', 111.0000);
INSERT INTO test_counters (name, counter) VALUES ('test2', 222.0000);

INSERT INTO passports (passport_number)
VALUES ('MP111111');
INSERT INTO addresses (city)
VALUES ('Minsk');
INSERT INTO persons (name, passport_id, address_id)
VALUES ('Alex', 1, 1);
INSERT INTO companies (title)
VALUES ('IT-Company');
INSERT INTO person_companies (person_id, company_id)
VALUES (1, 1);

INSERT INTO passports (passport_number)
VALUES ('MP222222');
INSERT INTO addresses (city)
VALUES ('Minsk');
INSERT INTO persons (name, passport_id, address_id)
VALUES ('Alex', 2, 2);
INSERT INTO companies (title)
VALUES ('AGRO-Company');
INSERT INTO person_companies (person_id, company_id)
VALUES (2, 2);