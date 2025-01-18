--liquibase formatted sql

-- changeset author:01-ticker-add
INSERT INTO ticker(name, company_name)
VALUES ('AAPL', 'Apple Inc.'),
       ('GOOGL', 'Alphabet Inc.'),
       ('MSFT', 'Microsoft Corp'),
       ('NVDA', 'NVIDIA Corp');