# Money Transfer REST API

RESTful API for money transfers between accounts.

### Requirements:
Java 10 or higher

### Frameworks
- Testing
    - Junit
    - Apache HTTP Client (for testing REST API)
- Logging 
    - Log4j 2
- Database
    - H2 in memory database
- Server
    - Embedded Jetty 
    - or Jetty by Spark
- REST Service
    - Jersey + JAXB for marshalling/unmarshalling
- Mapping model to DTO
    - Orika

### Project structure

#### Sources: 
Main packages for sources:
1. database - Database connections and utils.
2. embeddedserver - Embedded server realizations. Main method of each embeddedServer id "startServer"
3. rest - controllers, webModels. Each webModel mapped to DbEntity model via Orika
4. logic 
    - entity - java-entity for tables in Database. To use standard get/save dao must implements DbEntity (Core entity where each column mapped to java object field)
    - dao - Interfaces and Implementations for Data access objects.
    - service - second layer, uses Dao.

#### Resources:

- src/main/resources/applicaion.properties - App settings
- src/main/resources/dbmigration - Migration scripts
- src/main/resources/log4j2.xml - Log4j2 Configuration
- src/test/resources/jsonexamples - Json examples for testing REST API


### Configure

To configure change src/main/resources/application.properties file:
1. Properties db_* - for database settings
2. Properties server_* - for embedded server configuration

For configure db migration see src/main/resources/dbmigration. 

### Test
To start application use command:
```sh
gradlew test
```
src/test/java - tests sources

After test see build/reports/tests/test/index.html for results.
### Start
To start application use command:
```sh
gradlew run
```


### 

Application starts a jetty server on localhost port 8082 (by default). An H2 in-memory database initialized with account data.

- http://localhost:8082/rest/account

### Available Services

| HTTP METHOD | PATH | USAGE |
| -----------| ------ | ------ |
| GET | /account/{accountId} | get account by accountId | 
| GET | /account | get all accounts | 
| POST| /account | create a new account
| POST | /account/transfer | perform transfer between 2 accounts | 

### Http Status
- 200 OK: The request has succeeded
- 400 Bad Request: The request could not be understood by the server 
- 404 Not Found: The requested resource cannot be found
- 500 Internal Server Error: The server encountered an unexpected condition 

### Sample JSON for  Account

##### Account: : 

See src/test/resources/jsonexamples/accountcontroller

```sh
{
  "accountNumber": "4123567891234567",
  "balance": 100.00
}
```

##### Transfer:

See src/test/resources/jsonexamples/accountcontroller

```sh
{
  "accountFromId" : 1,
  "accountToId" : 2,
  "amount" : 12,
  "currencyCode" : "RUR"
}
```
