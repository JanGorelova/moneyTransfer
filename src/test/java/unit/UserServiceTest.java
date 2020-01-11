package unit;

import com.moneytransfer.model.dto.entity.UserDTO;
import com.moneytransfer.model.dto.request.UserCreationDTO;
import com.moneytransfer.model.entity.User;
import com.moneytransfer.service.UserService;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import util.DataGenerationUtil;

import java.time.LocalDateTime;
import java.util.Objects;

import static org.mockito.Mockito.mock;
import static org.powermock.api.mockito.PowerMockito.when;
import static org.powermock.api.mockito.PowerMockito.whenNew;

@RunWith(PowerMockRunner.class)
@PrepareForTest({UserService.class, User.class})
public class UserServiceTest {
    @InjectMocks
    private UserService userService;

    @Test
    public void testUserSuccessfullyCreated() throws Exception {
        UserCreationDTO userCreationDTO = DataGenerationUtil.generateUserCreationDTO();
        LocalDateTime localDateTime = LocalDateTime.now();
        User user = mock(User.class);

        whenNew(User.class)
                .withAnyArguments()
                .thenReturn(user);
        when(Objects.requireNonNull(user).saveIt()).thenReturn(true);

        when(user.getFirstName()).thenReturn("TestFirstName");
        when(user.getLastName()).thenReturn("TestLastName");
        when(user.getEmail()).thenReturn("Test@test.com");

        when(user.getDateCreated()).thenReturn(localDateTime);
        when(user.getDateUpdated()).thenReturn(localDateTime);

        UserDTO userDTO = userService.create(userCreationDTO);

        Assert.assertEquals("TestFirstName", userDTO.getFirstName());
        Assert.assertEquals( "TestLastName", userDTO.getLastName());
        Assert.assertEquals("Test@test.com", userDTO.getEmail());
        Assert.assertEquals(localDateTime, userDTO.getDateCreated());
        Assert.assertEquals(localDateTime, userDTO.getDateUpdated());
    }
}
