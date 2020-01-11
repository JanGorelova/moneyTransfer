package integration;

import org.junit.Before;
import org.junit.Test;
import util.ApplicationInitializationUtil;

import java.io.IOException;

public class AccountIntegrationTest {
    @Before
    public void setUp() {
        ApplicationInitializationUtil.initialize();
    }

    @Test
    public void testAccountSuccessfullyCreated() throws IOException {

    }

    @Test
    public void testAccountCreationFailedUserNotFound() throws IOException {

    }

    @Test
    public void testAccountCreationFailedAccountAlreadyExists() throws IOException {

    }

    @Test
    public void testDeposit() throws IOException {

    }

    @Test
    public void testDepositFailedAccountNotFound() throws IOException {

    }

    @Test
    public void testTransfer() throws IOException {

    }

    @Test
    public void testTransferSenderNotFound() throws IOException {

    }

    @Test
    public void testTransferRecipientNotFound() throws IOException {

    }

    @Test
    public void testTransferNotEnoughFunds() throws IOException {

    }

    @Test
    public void testGetTransactions() throws IOException {

    }
}
