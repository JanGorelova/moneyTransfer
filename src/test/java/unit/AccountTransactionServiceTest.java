package unit;

import com.moneytransfer.model.dto.entity.AccountTransactionDTO;
import com.moneytransfer.model.dto.request.AccountTransferDTO;
import com.moneytransfer.model.entity.AccountTransaction;
import com.moneytransfer.model.enums.Currency;
import com.moneytransfer.model.enums.TransactionType;
import com.moneytransfer.service.AccountTransactionService;
import io.vavr.control.Try;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import util.DataGenerationUtil;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Objects;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.powermock.api.mockito.PowerMockito.when;
import static org.powermock.api.mockito.PowerMockito.whenNew;

@RunWith(PowerMockRunner.class)
@PrepareForTest({AccountTransactionService.class})
public class AccountTransactionServiceTest {
    @InjectMocks
    private AccountTransactionService accountTransactionService;

    @Test
    public void testAccountTransactionSuccessfullyCreated() {
        AccountTransferDTO userCreationDTO = DataGenerationUtil.generateAccountTransferDTO();
        LocalDateTime localDateTime = LocalDateTime.now();
        AccountTransaction accountTransaction = mock(AccountTransaction.class);

        Try.run(() -> whenNew(AccountTransaction.class)
                .withAnyArguments()
                .thenReturn(accountTransaction));

        when(Objects.requireNonNull(accountTransaction).saveIt()).thenReturn(true);

        when(accountTransaction.getSenderId()).thenReturn(1L);
        when(accountTransaction.getRecipientId()).thenReturn(2L);
        when(accountTransaction.getCurrency()).thenReturn(Currency.USD);
        when(accountTransaction.getAmount()).thenReturn(BigDecimal.ONE);
        when(accountTransaction.getTransactionType()).thenReturn(TransactionType.INTERNAL);
        when(accountTransaction.getDateCreated()).thenReturn(localDateTime);

        AccountTransactionDTO accountTransactionDTO = accountTransactionService.create(userCreationDTO);

        assertEquals(Long.valueOf(1L), accountTransactionDTO.getSenderId());
        assertEquals(Long.valueOf(2L), accountTransactionDTO.getRecipientId());
        assertEquals(Currency.USD, accountTransactionDTO.getCurrency());
        assertEquals(BigDecimal.ONE, accountTransactionDTO.getAmount());
        assertEquals(TransactionType.INTERNAL, accountTransactionDTO.getTransactionType());
        assertEquals(localDateTime, accountTransactionDTO.getDateCreated());
    }
}
