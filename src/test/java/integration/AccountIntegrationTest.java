package integration;

import com.moneytransfer.configuration.database.DatabaseConfiguration;
import com.moneytransfer.model.entity.Account;
import com.moneytransfer.model.enums.Currency;
import com.moneytransfer.util.ApplicationUtil;
import com.moneytransfer.util.RateUtil;
import constants.Constants;
import dto.ResponseDTO;
import io.javalin.Javalin;
import org.eclipse.jetty.http.HttpStatus;
import org.javalite.activejdbc.DB;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import util.DataGenerationUtil;
import util.HttpRequestUtil;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.LocalDate;

public class AccountIntegrationTest {
    private static Javalin application;

    @Before
    public void setup() {
        application = ApplicationUtil.start(Constants.TEST_PROPERTIES);
    }

    @After
    public void stopApplication() {
        application.stop();
    }

    @Test
    public void testAccountSuccessfullyCreated() {
        HttpRequestUtil.launchPost(Constants.USER_CREATION_URL, DataGenerationUtil.generateUserCreationDTOJson("Test@test.com"));

        ResponseDTO responseDTO = HttpRequestUtil.launchPost(Constants.ACCOUNT_CREATION_URL,
                DataGenerationUtil.generateAccountCreationDTOJson(1L, Currency.RUB.name()));
        String accountDTOJson = responseDTO.getEntity();

        Assert.assertEquals(HttpStatus.OK_200, responseDTO.getStatus());

        Assert.assertTrue(accountDTOJson.contains("\"id\":1"));
        Assert.assertTrue(accountDTOJson.contains("\"userId\":1"));
        Assert.assertTrue(accountDTOJson.contains("\"balance\":0"));
        Assert.assertTrue(accountDTOJson.contains("\"currency\":\"RUB\""));
        Assert.assertTrue(accountDTOJson.contains("\"dateCreated\":\"" + LocalDate.now()));
        Assert.assertTrue(accountDTOJson.contains("\"dateUpdated\":\"" + LocalDate.now()));

        DB connection = DatabaseConfiguration.getConnection();
        Assert.assertFalse(Account.where("user_id = ?", 1L).isEmpty());
        connection.close();
    }

    @Test
    public void testAccountCreationFailedUserNotFound() {
        ResponseDTO responseDTO = HttpRequestUtil.launchPost(Constants.ACCOUNT_CREATION_URL,
                DataGenerationUtil.generateAccountCreationDTOJson(1L, Currency.RUB.name()));

        Assert.assertEquals(HttpStatus.INTERNAL_SERVER_ERROR_500, responseDTO.getStatus());
        Assert.assertTrue(responseDTO.getEntity().contains("\"message\":\"User with specified id: 1 doesn't exist\""));
    }

    @Test
    public void testAccountCreationFailedAccountAlreadyExists() {
        HttpRequestUtil.launchPost(Constants.USER_CREATION_URL, DataGenerationUtil.generateUserCreationDTOJson("Test@test.com"));
        HttpRequestUtil.launchPost(Constants.ACCOUNT_CREATION_URL, DataGenerationUtil.generateAccountCreationDTOJson(1L, Currency.RUB.name()));

        ResponseDTO responseDTO = HttpRequestUtil.launchPost(Constants.ACCOUNT_CREATION_URL,
                DataGenerationUtil.generateAccountCreationDTOJson(1L, Currency.EUR.name()));

        Assert.assertEquals(HttpStatus.INTERNAL_SERVER_ERROR_500, responseDTO.getStatus());
        Assert.assertTrue(responseDTO.getEntity().contains("\"message\":\"User with specified id: 1 already has account\""));
    }

    @Test
    public void testDeposit() {
        HttpRequestUtil.launchPost(Constants.USER_CREATION_URL, DataGenerationUtil.generateUserCreationDTOJson("Test@test.com"));
        HttpRequestUtil.launchPost(Constants.ACCOUNT_CREATION_URL, DataGenerationUtil.generateAccountCreationDTOJson(1L, Currency.RUB.name()));

        BigDecimal amount = BigDecimal.ONE;
        String currency = Currency.EUR.name();
        ResponseDTO responseDTO = HttpRequestUtil.launchPost(Constants.ACCOUNT_DEPOSIT_URL,
                DataGenerationUtil.generateAccountDepositDTOJson(1L, BigDecimal.ONE, currency));
        BigInteger exchanged = RateUtil.exchange(Currency.RUB, Currency.EUR, amount).toBigInteger();

        String accountDTOJson = responseDTO.getEntity();

        Assert.assertEquals(HttpStatus.OK_200, responseDTO.getStatus());

        Assert.assertTrue(accountDTOJson.contains("\"userId\":1"));
        Assert.assertTrue(accountDTOJson.contains(String.format("\"balance\":%d", exchanged)));
        Assert.assertTrue(accountDTOJson.contains("\"currency\":\"RUB\""));
        Assert.assertTrue(accountDTOJson.contains("\"dateCreated\":\"" + LocalDate.now()));
        Assert.assertTrue(accountDTOJson.contains("\"dateUpdated\":\"" + LocalDate.now()));

        DB connection = DatabaseConfiguration.getConnection();
        BigInteger balance = Account.where("user_id = ?", 1L).get(0).getBigDecimal("balance").toBigInteger();
        Assert.assertEquals(exchanged, balance);
        connection.close();
    }

    @Test
    public void testDepositFailedAccountNotFound() {
        ResponseDTO responseDTO = HttpRequestUtil.launchPost(Constants.ACCOUNT_DEPOSIT_URL,
                DataGenerationUtil.generateAccountDepositDTOJson(1L, BigDecimal.ONE, Currency.RUB.name()));

        Assert.assertEquals(HttpStatus.INTERNAL_SERVER_ERROR_500, responseDTO.getStatus());
        Assert.assertTrue(responseDTO.getEntity().contains("\"message\":\"Account with id: 1 doesn't exist\""));
    }

    @Test
    public void testTransfer() {
        HttpRequestUtil.launchPost(Constants.USER_CREATION_URL, DataGenerationUtil.generateUserCreationDTOJson("Test@test.com"));
        HttpRequestUtil.launchPost(Constants.USER_CREATION_URL, DataGenerationUtil.generateUserCreationDTOJson("Test2@test.com"));

        HttpRequestUtil.launchPost(Constants.ACCOUNT_CREATION_URL, DataGenerationUtil.generateAccountCreationDTOJson(1L, Currency.RUB.name()));
        HttpRequestUtil.launchPost(Constants.ACCOUNT_CREATION_URL, DataGenerationUtil.generateAccountCreationDTOJson(2L, Currency.USD.name()));

        HttpRequestUtil.launchPost(Constants.ACCOUNT_DEPOSIT_URL,
                DataGenerationUtil.generateAccountDepositDTOJson(1L, BigDecimal.TEN, Currency.EUR.name()));

        BigInteger depositedOnFirstAccount = RateUtil.exchange(Currency.RUB, Currency.EUR, BigDecimal.TEN).toBigInteger();
        BigInteger transferFromFirstAccount = RateUtil.exchange(Currency.RUB, Currency.EUR, BigDecimal.ONE).toBigInteger();
        BigInteger depositToSecondAccount = RateUtil.exchange(Currency.USD, Currency.EUR, BigDecimal.ONE).toBigInteger();

        ResponseDTO responseDTO = HttpRequestUtil.launchPost(Constants.ACCOUNT_TRANSFER_URL,
                DataGenerationUtil.generateAccountTransferDTOJson(1L, 2L, BigDecimal.ONE, Currency.EUR.name()));

        String accountTransactionDTOJson = responseDTO.getEntity();

        Assert.assertEquals(HttpStatus.OK_200, responseDTO.getStatus());

        Assert.assertTrue(accountTransactionDTOJson.contains("\"senderId\":1"));
        Assert.assertTrue(accountTransactionDTOJson.contains("\"recipientId\":2"));
        Assert.assertTrue(accountTransactionDTOJson.contains(String.format("\"amount\":%d", 1)));
        Assert.assertTrue(accountTransactionDTOJson.contains("\"currency\":\"EUR\""));
        Assert.assertTrue(accountTransactionDTOJson.contains("\"transactionType\":\"INTERNAL\""));
        Assert.assertTrue(accountTransactionDTOJson.contains("\"dateCreated\":\"" + LocalDate.now()));

        DB connection = DatabaseConfiguration.getConnection();
        BigInteger firstAccountBalance = Account.where("user_id = ?", 1L).get(0).getBigDecimal("balance").toBigInteger();
        BigInteger secondAccountBalance = Account.where("user_id = ?", 2L).get(0).getBigDecimal("balance").toBigInteger();

        Assert.assertEquals(depositedOnFirstAccount.subtract(transferFromFirstAccount), firstAccountBalance);
        Assert.assertEquals(depositToSecondAccount, secondAccountBalance);
        connection.close();
    }

    @Test
    public void testTransferSenderNotFound() {
        ResponseDTO responseDTO = HttpRequestUtil.launchPost(Constants.ACCOUNT_TRANSFER_URL,
                DataGenerationUtil.generateAccountTransferDTOJson(1L, 2L, BigDecimal.ONE, Currency.EUR.name()));

        Assert.assertEquals(HttpStatus.INTERNAL_SERVER_ERROR_500, responseDTO.getStatus());
        Assert.assertTrue(responseDTO.getEntity().contains("\"message\":\"Account with id: 1 doesn't exist\""));
    }

    @Test
    public void testTransferRecipientNotFound() {
        HttpRequestUtil.launchPost(Constants.USER_CREATION_URL, DataGenerationUtil.generateUserCreationDTOJson("Test@test.com"));
        HttpRequestUtil.launchPost(Constants.ACCOUNT_CREATION_URL, DataGenerationUtil.generateAccountCreationDTOJson(1L, Currency.RUB.name()));

        ResponseDTO responseDTO = HttpRequestUtil.launchPost(Constants.ACCOUNT_TRANSFER_URL,
                DataGenerationUtil.generateAccountTransferDTOJson(1L, 2L, BigDecimal.ONE, Currency.EUR.name()));

        Assert.assertEquals(HttpStatus.INTERNAL_SERVER_ERROR_500, responseDTO.getStatus());
        Assert.assertTrue(responseDTO.getEntity().contains("\"message\":\"Account with id: 2 doesn't exist\""));
    }

    @Test
    public void testTransferNotEnoughFunds() {
        HttpRequestUtil.launchPost(Constants.USER_CREATION_URL, DataGenerationUtil.generateUserCreationDTOJson("Test@test.com"));
        HttpRequestUtil.launchPost(Constants.USER_CREATION_URL, DataGenerationUtil.generateUserCreationDTOJson("Test2@test.com"));

        HttpRequestUtil.launchPost(Constants.ACCOUNT_CREATION_URL, DataGenerationUtil.generateAccountCreationDTOJson(1L, Currency.RUB.name()));
        HttpRequestUtil.launchPost(Constants.ACCOUNT_CREATION_URL, DataGenerationUtil.generateAccountCreationDTOJson(2L, Currency.USD.name()));

        HttpRequestUtil.launchPost(Constants.ACCOUNT_DEPOSIT_URL,
                DataGenerationUtil.generateAccountDepositDTOJson(1L, BigDecimal.ONE, Currency.EUR.name()));

        ResponseDTO responseDTO = HttpRequestUtil.launchPost(Constants.ACCOUNT_TRANSFER_URL,
                DataGenerationUtil.generateAccountTransferDTOJson(1L, 2L, BigDecimal.TEN, Currency.EUR.name()));

        Assert.assertEquals(HttpStatus.INTERNAL_SERVER_ERROR_500, responseDTO.getStatus());
        Assert.assertTrue(responseDTO.getEntity().contains("\"message\":\"Sender with account id: 1 has not enough funds for transfer\""));
    }

    @Test
    public void testGetTransactions() {
        HttpRequestUtil.launchPost(Constants.USER_CREATION_URL, DataGenerationUtil.generateUserCreationDTOJson("Test@test.com"));
        HttpRequestUtil.launchPost(Constants.USER_CREATION_URL, DataGenerationUtil.generateUserCreationDTOJson("Test2@test.com"));

        HttpRequestUtil.launchPost(Constants.ACCOUNT_CREATION_URL, DataGenerationUtil.generateAccountCreationDTOJson(1L, Currency.RUB.name()));
        HttpRequestUtil.launchPost(Constants.ACCOUNT_CREATION_URL, DataGenerationUtil.generateAccountCreationDTOJson(2L, Currency.USD.name()));

        HttpRequestUtil.launchPost(Constants.ACCOUNT_DEPOSIT_URL,
                DataGenerationUtil.generateAccountDepositDTOJson(1L, BigDecimal.TEN, Currency.EUR.name()));
        HttpRequestUtil.launchPost(Constants.ACCOUNT_TRANSFER_URL,
                DataGenerationUtil.generateAccountTransferDTOJson(1L, 2L, BigDecimal.ONE, Currency.EUR.name()));

        ResponseDTO responseDTO = HttpRequestUtil.launchGet(Constants.ACCOUNT_TRANSACTIONS_URL);

        String accountTransactionDTOJson = responseDTO.getEntity();

        Assert.assertEquals(HttpStatus.OK_200, responseDTO.getStatus());

        Assert.assertTrue(accountTransactionDTOJson.contains("\"recipientId\":1"));
        Assert.assertTrue(accountTransactionDTOJson.contains(String.format("\"amount\":%s", 10.0000000)));
        Assert.assertTrue(accountTransactionDTOJson.contains("\"currency\":\"EUR\""));
        Assert.assertTrue(accountTransactionDTOJson.contains("\"transactionType\":\"DEPOSIT\""));
        Assert.assertTrue(accountTransactionDTOJson.contains("\"dateCreated\":\"" + LocalDate.now()));

        Assert.assertTrue(accountTransactionDTOJson.contains("\"senderId\":1"));
        Assert.assertTrue(accountTransactionDTOJson.contains("\"recipientId\":2"));
        Assert.assertTrue(accountTransactionDTOJson.contains(String.format("\"amount\":%s", 1.0000000)));
        Assert.assertTrue(accountTransactionDTOJson.contains("\"currency\":\"EUR\""));
        Assert.assertTrue(accountTransactionDTOJson.contains("\"transactionType\":\"INTERNAL\""));
        Assert.assertTrue(accountTransactionDTOJson.contains("\"dateCreated\":\"" + LocalDate.now()));
    }
}
