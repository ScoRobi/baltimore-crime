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
To run, simply execute the 'run.sh' script in the main directory and navigate to `localhost:8080` in a browser. However, if this fails you can run the project manually with the following.

In the 'baltimore-crime-core' directory
* Execute: `gradle bootRun`
  * NOTE: MySql must be running, populated, and available at `localhost:3306` (its default location).
* In a browser, navigate to `localhost:8080`

