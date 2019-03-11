package com.tsoyolv.transfermoney;

public interface UriPath {
    String SERVER_ROOT_PATH = "/";
    String REST_ROOT_PATH = "/rest";

    String ACCOUNT_ROOT_PATH = "/account";
    String GET_ACCOUNT_BY_ID_PATH = "/{accountId}";
    String TRANSFER_BETWEEN_ACCOUNTS_PATH = "/transfer";
}
