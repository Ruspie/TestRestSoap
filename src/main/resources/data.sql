/*TRUNCATE TABLE person_companies;
TRUNCATE TABLE users;
TRUNCATE TABLE test_counters;
TRUNCATE TABLE companies;
TRUNCATE TABLE addresses;
TRUNCATE TABLE passports;
TRUNCATE TABLE persons;
*/
INSERT INTO test_counters (name, counter) VALUES ('test1', 111.0000);
INSERT INTO test_counters (name, counter) VALUES ('test2', 222.0000);

INSERT INTO passports (passport_number)
VALUES ('MP111111');
INSERT INTO addresses (city)
VALUES ('Minsk');
INSERT INTO persons (name, passport_id, address_id, age)
VALUES ('Ilya', 1, 1, 18);
INSERT INTO companies (title)
VALUES ('IT-Company');
INSERT INTO person_companies (person_id, company_id)
VALUES (1, 1);

INSERT INTO passports (passport_number)
VALUES ('MP222222');
INSERT INTO addresses (city)
VALUES ('Minsk');
INSERT INTO persons (name, passport_id, address_id, age)
VALUES ('Ivan', 2, 2, 24);
INSERT INTO companies (title)
VALUES ('Bank');
INSERT INTO person_companies (person_id, company_id)
VALUES (2, 2);

INSERT INTO passports (passport_number)
VALUES ('MP333333');
INSERT INTO addresses (city)
VALUES ('Moscow');
INSERT INTO persons (name, passport_id, address_id, age)
VALUES ('Petr', 3, 3, 26);
INSERT INTO companies (title)
VALUES ('Factory');
INSERT INTO person_companies (person_id, company_id)
VALUES (3, 3);

INSERT INTO passports (passport_number)
VALUES ('MP444444');
INSERT INTO addresses (city)
VALUES ('Minsk');
INSERT INTO persons (name, passport_id, address_id, age)
VALUES ('Alex', 4, 4, 24);
INSERT INTO companies (title)
VALUES ('Bank');
INSERT INTO person_companies (person_id, company_id)
VALUES (4, 4);

-- Связываем аккаунт 'user' с Ильей (person_id = 1)
INSERT INTO users (username, password, role, person_id)
VALUES ('user', '$2a$10$8.g9I.qCisw7qWnClyv7AOnN9v2uM7K8i7N5k6h9oR8A0P1Gz6Z2.', 'ROLE_USER', 1);

-- Связываем аккаунт 'admin' с Иваном (person_id = 2)
INSERT INTO users (username, password, role, person_id)
VALUES ('admin', '$2a$10$8.g9I.qCisw7qWnClyv7AOnN9v2uM7K8i7N5k6h9oR8A0P1Gz6Z2.', 'ROLE_ADMIN', 2);