INSERT INTO countries (country_id, country_name, country_code)
VALUES (1, 'Myanmar', 'MM');
INSERT INTO countries (country_id, country_name, country_code)
VALUES (2, 'Nicaragua', 'NI');
INSERT INTO countries (country_id, country_name, country_code)
VALUES (3, 'Guernsey', 'GG');
INSERT INTO countries (country_id, country_name, country_code)
VALUES (4, 'Eritrea', 'ER');
INSERT INTO countries (country_id, country_name, country_code)
VALUES (5, 'Cuba', 'CU');
INSERT INTO countries (country_id, country_name, country_code)
VALUES (6, 'Morocco', 'MA');
INSERT INTO countries (country_id, country_name, country_code)
VALUES (7, 'South Sudan', 'SS');

INSERT INTO status (status_id, state_name, description)
VALUES (1, 'Active', 'Active status');
INSERT INTO status (status_id, state_name, description)
VALUES (2, 'Inactive', 'Inactive status');

INSERT INTO player (player_id, registration_date, status, country, balance, currency)
VALUES (1, TIMESTAMP '2018-05-16 15:36:38', 1, 1, 1000.0, 'UAH');
INSERT INTO player (player_id, registration_date, status, country, balance, currency)
VALUES (2, TIMESTAMP '2019-02-11 12:32:01', 2, 2, 2000.0, 'UYU');
INSERT INTO player (player_id, registration_date, status, country, balance, currency)
VALUES (3, TIMESTAMP '2020-11-08 09:45:17', 1, 3, 3000.0, 'USD');
INSERT INTO player (player_id, registration_date, status, country, balance, currency)
VALUES (4, TIMESTAMP '2021-08-15 00:16:32', 2, 4, 4000.0, 'UGX');
INSERT INTO player (player_id, registration_date, status, country, balance, currency)
VALUES (5, TIMESTAMP '2018-12-24 11:26:58', 1, 5, 5000.0, 'UZS');



