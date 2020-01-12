package integration;

import com.moneytransfer.configuration.database.DatabaseConfiguration;
import com.moneytransfer.model.entity.User;
import com.moneytransfer.util.ApplicationUtil;
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

import java.time.LocalDate;


public class UserIntegrationTest {
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
    public void testUserSuccessfullyCreated() {
        String userCreationDTOJson = DataGenerationUtil.generateUserCreationDTOJson("Test@test.com");

        ResponseDTO responseDTO = HttpRequestUtil.launchPost(Constants.USER_CREATION_URL, userCreationDTOJson);
        String userDTOJson = responseDTO.getEntity();

        Assert.assertEquals(HttpStatus.OK_200, responseDTO.getStatus());

        Assert.assertTrue(userDTOJson.contains("\"firstName\":\"TestFirstName\""));
        Assert.assertTrue(userDTOJson.contains("\"lastName\":\"TestLastName\""));
        Assert.assertTrue(userDTOJson.contains("\"email\":\"Test@test.com\""));
        Assert.assertTrue(userDTOJson.contains("\"dateCreated\":\"" + LocalDate.now()));
        Assert.assertTrue(userDTOJson.contains("\"dateUpdated\":\"" + LocalDate.now()));

        DB conn = DatabaseConfiguration.getConnection();
        Assert.assertFalse(User.where("email = ?", "Test@test.com").isEmpty());
        conn.close();
    }

    @Test
    public void testUserCreationFailedEmailAlreadyExists() {
        String userCreationDTOJson = DataGenerationUtil.generateUserCreationDTOJson("Test@test.com");
        HttpRequestUtil.launchPost(Constants.USER_CREATION_URL, userCreationDTOJson);

        ResponseDTO responseDTO = HttpRequestUtil.launchPost(Constants.USER_CREATION_URL, userCreationDTOJson);

        Assert.assertEquals(HttpStatus.INTERNAL_SERVER_ERROR_500, responseDTO.getStatus());
        Assert.assertTrue(responseDTO.getEntity().contains("\"message\":\"User with specified email: Test@test.com already exists\""));
    }
}
