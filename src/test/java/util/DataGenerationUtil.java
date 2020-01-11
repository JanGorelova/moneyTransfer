package util;

import com.moneytransfer.model.dto.request.UserCreationDTO;

public class DataGenerationUtil {
    public static UserCreationDTO generateUserCreationDTO() {
        return UserCreationDTO.builder()
                .firstName("TestFirstName")
                .lastName("TestLastName")
                .email("Test@test.com")
                .build();
    }

    public static String userCreationDTOJson() {
        return " {\"firstName\" : \"TestFirstName\",\n" +
                "\"lastName\": \"TestLastName\",\n" +
                "\"email\": \"Test@test.com\"}";
    }
}
