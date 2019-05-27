# rest-assured-untappd
Rest Assured example with Untappd API. Project uses Gradle, JUnit5 and Allure reports.

# Untappd
Social network for discovering and sharing your favorite beer: https://untappd.com/

API docs: https://untappd.com/api/docs

# Run tests
This example is based on the API fetched from Untappd iOS app.

To run the tests, first you need to complete config:
* Get client_id and client_secret values and put them into `env.properties` file. 
* Create user, and put username and password into `testdata.properties` file.

Run tests with command `./gradlew clean test -Denvironment="dev"`, where environment argument should be "prod" to use production env.properties file, or anything else to use development env.properties file.

Create Allure reports with `/gradlew allureReport` command.

Display Allure reports with `./gradlew allureServe` command
