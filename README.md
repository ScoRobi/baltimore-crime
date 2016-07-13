# baltimore-crime

## Dependencies

#### MySql
1.) Download and install MySql from http://www.mysql.com/downloads/.
2.) Setup user "root" with no password.
3.) Run MySql server.
4.) Run the following scripts in listed order to populate the database:
	a.) CREATE_DB_BALTIMORE_CRIME.sql
	b.) CREATE_TABLE_CRIME.sql
	c.) INSERT_JUNE_1_TO_18_v2.sql
	
	The following is executable from the root directory, 'baltimore-crime/':
	mysql -u "root" < baltimore-crime-mysql/CREATE_DB_BALTIMORE_CRIME.sql
	mysql -u "root" < baltimore-crime-mysql/CREATE_TABLE_CRIME.sql
	mysql -u "root" < baltimore-crime-mysql/INSERT_JUNE_1_TO_18_2016_v2.sql
	
## Run
To run, simply execute the 'run.sh' script in the main directory (Note: a UI refresh may be necessary once the backend fully deploys). However, if this fails you can run each project independently with the following.

#### Core
In the 'baltimore-crime-core' directory, execute: `gradle bootRun`

#### UI
In the 'baltimore-crime-ui' directory, execute: `open webpage/index.html`