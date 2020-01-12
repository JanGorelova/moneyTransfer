package util;

import com.moneytransfer.model.dto.request.AccountTransferDTO;
import com.moneytransfer.model.enums.Currency;
import com.moneytransfer.model.enums.TransactionType;

import java.math.BigDecimal;

public class DataGenerationUtil {
    public static AccountTransferDTO generateAccountTransferDTO() {
        return AccountTransferDTO.builder()
                .senderAccountId(1L)
                .recipientAccountId(2L)
                .currency(Currency.USD)
                .amount(BigDecimal.ONE)
                .transactionType(TransactionType.INTERNAL)
                .build();
    }

    public static String generateAccountCreationDTOJson(Long userId, String currency) {
        return String.format("{\"userId\" : %d,\"currency\": \"%s\"}", userId, currency);
    }

    public static String generateUserCreationDTOJson(String email) {
        return " {\"firstName\" : \"TestFirstName\",\n" +
                "\"lastName\": \"TestLastName\",\n" +
                "\"email\": \"" + email + "\"}";
    }

    public static String generateAccountDepositDTOJson(Long accountId, BigDecimal amount, String currency) {
        return String.format("{\"recipientAccountId\" : %d, \"currency\": \"%s\",\"amount\": %s}",
                accountId, currency, amount);
    }

    public static String generateAccountTransferDTOJson(Long senderAccountId, Long recipientAccountId, BigDecimal amount,
                                                        String currency) {
        return String.format("{ \"senderAccountId\" : %d,\n" +
                        "\"recipientAccountId\": %d,\n" +
                        "\"amount\": %s,\n" +
                        "\"currency\":\"%s\",\n" +
                        "\"transactionType\":\"INTERNAL\"}",
                senderAccountId, recipientAccountId, amount, currency);
    }
}
