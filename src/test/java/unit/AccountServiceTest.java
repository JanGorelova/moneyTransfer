package unit;

import com.moneytransfer.model.entity.User;
import com.moneytransfer.service.UserService;
import org.javalite.activejdbc.Model;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

@RunWith(PowerMockRunner.class)
@PrepareForTest({UserService.class, User.class, Model.class})
public class AccountServiceTest {
    @Test
    public void testAccountSuccessfullyCreated() {

    }
}
