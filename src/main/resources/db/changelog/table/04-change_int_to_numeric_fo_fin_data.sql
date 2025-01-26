--liquibase formatted sql

-- changeset author:04-change_int_to_numeric_fo_fin_data
alter table ticker_data
alter column open type NUMERIC,
alter column close type NUMERIC,
alter column high type NUMERIC,
alter column low type NUMERIC;