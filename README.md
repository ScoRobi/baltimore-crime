# baltimore-crime

## Dependencies

#### MySql
1. Download and install MySql from http://www.mysql.com/downloads/.
2. Setup user "root" with no password.
3. Run MySql server.
4. Run the following scripts in listed order to populate the database:
  1. `CREATE_DB_BALTIMORE_CRIME.sql`
  2. `CREATE_TABLE_CRIME.sql`
  3. `INSERT_JUNE_1_TO_18_v2.sql`
	
     The following is executable from the root directory to create and populate the database:
     ```
     mysql -u "root" < baltimore-crime-mysql/CREATE_DB_BALTIMORE_CRIME.sql
     mysql -u "root" < baltimore-crime-mysql/CREATE_TABLE_CRIME.sql
     mysql -u "root" < baltimore-crime-mysql/INSERT_JUNE_1_TO_18_2016_v2.sql
     ```
	
## Run
To run, simply execute the 'run.sh' script in the main directory (**note**: a UI refresh may be necessary once the backend fully deploys). However, if this fails you can run each project independently with the following.

#### Run Core Independently
In the 'baltimore-crime-core' directory, execute: `gradle bootRun`
* The Core should always be deployed at `localhost:8080` to be properly accessed by the UI.
* MySql must be running, populated, and available at `localhost:3306` (its default location).

#### Run UI Independently
In the 'baltimore-crime-ui' directory, execute: `open webpage/index.html`
* The UI requires that the Core is running and available at `localhost:8080` in order to populate the map and apply filtering.
* An internet connection is required for the UI to pull needed libraries.
