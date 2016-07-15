DROP TABLE IF EXISTS baltimore_crime.crime;

CREATE TABLE baltimore_crime.crime (
	sid BIGINT,
	id VARCHAR(40),
	position BIGINT,
	created_at BIGINT,
	created_meta VARCHAR(10),
	updated_at BIGINT,
	updated_meta VARCHAR(10),
	meta VARCHAR(128),
	crimeDate DATE,
    crimeTime TIME,
	crimeCode VARCHAR(8),
	location VARCHAR(128),
    description VARCHAR(8),
	weapon VARCHAR(16),
	post VARCHAR(8),
	district VARCHAR(32),
    neighborhood VARCHAR(64),
	location1 BIGINT,
	totalIncidents BIGINT,
	lat DOUBLE(32,16),
	lon DOUBLE(32,16),
	meta2 VARCHAR(128),
	meta3 VARCHAR(128)
);
