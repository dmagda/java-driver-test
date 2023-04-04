# JDBC Query Modes Test

Simple app that tests various JDBC query modes.

## Prerequisite
* Java Development Kit, version 17 or later
* Maven 3.0 or later
* Command line tool or your favourite IDE, such as IntelliJ IDEA, or Eclipse.

## How to Use


Open the `app.properties` file and specify the following configuration parameters:
* `host` - the hostname of your cluster instance.
* `port` - the port number that will be used by the JDBC driver
* `dbUser` - the database username you used for your instance.
* `dbPassword` - the database password.

Next, build and run the app:

1. Build the app with Maven:
    ```bash
    mvn clean package
    ```
2. Run the app:
    ```bash
    java -cp target/driver-modes-test-1.0-SNAPSHOT.jar SampleApp
    ```
3. Follow the prompt by providing a protocol mode and hitting the Enter button.