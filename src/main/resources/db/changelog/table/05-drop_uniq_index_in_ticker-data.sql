--liquibase formatted sql

-- changeset author:05-drop_uniq_index_in_ticker-data
drop index ticker_data_unique_user_ticker;