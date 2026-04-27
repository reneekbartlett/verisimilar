CREATE DATABASE IF NOT EXISTS rb;
CREATE SCHEMA IF NOT EXISTS rb;
SET search_path TO rb;

CREATE TABLE IF NOT EXISTS config_fullname_last_census (
    id bigint NOT NULL PRIMARY KEY,
    last_name character varying(255),
    year integer NOT NULL,
    rank integer NOT NULL,
    count integer,
    prop100k numeric(7,2),
    cum_prop100k numeric(7,2),
    pctwhite numeric(5,2),
    pctblack numeric(5,2),
    pctapi numeric(5,2),
    pctaian numeric(5,2),
    pct2prace numeric(5,2),
    pcthispanic numeric(5,2)
);

INSERT INTO config_fullname_last_census (last_name, year, rank, count) VALUES ('BARTLETT', 2010, 2, 1);
