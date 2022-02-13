CREATE USER kickstart PASSWORD 'kickstart';

CREATE DATABASE kickstart;
GRANT ALL PRIVILEGES ON DATABASE kickstart TO kickstart;

CREATE DATABASE kickstart_test;
GRANT ALL PRIVILEGES ON DATABASE kickstart_test TO kickstart;

CREATE DATABASE kickstart_acceptance;
GRANT ALL PRIVILEGES ON DATABASE kickstart_acceptance TO kickstart;
