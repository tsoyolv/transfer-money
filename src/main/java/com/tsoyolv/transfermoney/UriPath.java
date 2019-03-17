package com.tsoyolv.transfermoney;

public interface UriPath {
    String SERVER_ROOT_PATH = "/";
    String REST_ROOT_PATH = "/rest";

    String ACCOUNT_ROOT_PATH = "/account";
    String TRANSFER_BETWEEN_ACCOUNTS_PATH = "/transfer";

    String TRANSACTION_ROOT_PATH = "/transaction";

    String TEST_THREAD_SAFE_CONTROLLER = "/threadsafe";
}
