package com.tsoyolv.transfermoney.rest;

/**
 * Constants for Uri Paths.
 * Constant interfaces are good only in simple projects, otherwise there will be version control conflicts!
 */
public interface RestPaths {
    String REST_ROOT_PATH = "/rest";

    String ACCOUNT_ROOT_PATH = "/account";
    String TRANSFER_BETWEEN_ACCOUNTS_PATH = "/transfer";

    String TRANSACTION_ROOT_PATH = "/transaction";

    String TEST_THREAD_SAFE_CONTROLLER = "/threadsafe";
}
