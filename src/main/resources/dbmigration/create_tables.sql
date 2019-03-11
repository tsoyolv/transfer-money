-- USER
DROP TABLE IF EXISTS USER;

CREATE TABLE USER (
USERID    NUMBER(38)  NOT NULL  PRIMARY KEY AUTO_INCREMENT,
USERNAME  VARCHAR(30) NOT NULL,
EMAIL     VARCHAR(30) NOT NULL);

CREATE UNIQUE INDEX UX_USER_EMAIL_USERNAME ON USER(USERNAME, EMAIL);

-- ACCOUNT
DROP TABLE IF EXISTS ACCOUNT;

CREATE TABLE ACCOUNT (
ACCOUNTID       NUMBER(38) PRIMARY KEY AUTO_INCREMENT NOT NULL,
ACCOUNTNUMBER   VARCHAR(34),
USERNAME        VARCHAR(30),
BALANCE         NUMBER(18,2),
CURRENCYCODE    VARCHAR(30)
);

CREATE UNIQUE INDEX UX_ACCOUNT_USERNAME_CURRENCY ON ACCOUNT(USERNAME,CURRENCYCODE);