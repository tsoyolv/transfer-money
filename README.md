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

### Configure

To configure change src/main/resources/application.properties file:
1. Properties db_* - for database settings
2. Properties server_* - for embedded server configuration

### Start
To start application use command:
```sh
gradlew run
```


### 

Application starts a jetty server on localhost port 8082 (by default). An H2 in-memory database initialized with account data.

- http://localhost:8080/account

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

#### Transfer:

See src/test/resources/jsonexamples/accountcontroller

```sh
{
  "accountFrom" : 1,
  "accountTo" : 2,
  "amount" : 12,
  "currencyCode" : "RUR"
}
```
