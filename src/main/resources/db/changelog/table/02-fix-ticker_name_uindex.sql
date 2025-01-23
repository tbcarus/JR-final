--liquibase formatted sql

-- changeset author:02-fix-ticker_name_uindex
DROP INDEX ticker_name_uindex;
CREATE UNIQUE INDEX ticker_name_uindex ON ticker (name);