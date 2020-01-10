package integration;

import com.google.inject.Guice;
import com.moneytransfer.configuration.JavalinConfiguration;
import com.moneytransfer.configuration.database.DatabaseConfiguration;
import com.moneytransfer.configuration.googlejuice.AOPModule;
import com.moneytransfer.configuration.googlejuice.BasicModule;
import io.vavr.control.Try;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class UserIntegrationTest {
    @BeforeAll
    void setUp() {
        Properties properties = Try.withResources(
                () -> UserIntegrationTest.class.getClassLoader().getResourceAsStream("configuration_test.properties"))
                .of(UserIntegrationTest::getProperties)
                .getOrElseThrow( ex -> new RuntimeException(ex.getMessage()));

        Guice.createInjector(new BasicModule(), new AOPModule());

        DatabaseConfiguration.initializeDatabase(properties);
        JavalinConfiguration.startJavalin(properties);
    }

    private static Properties getProperties(InputStream inputStream) throws IOException {
        Properties properties = new Properties();

        properties.load(inputStream);

        return properties;
    }

    @Test
    void test() {
        System.out.println("HI");
    }


}
