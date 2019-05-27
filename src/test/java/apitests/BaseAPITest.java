package apitests;

import io.restassured.specification.RequestSpecification;
import org.junit.jupiter.api.BeforeAll;
import utils.TestDataUtils;

import java.util.Properties;

import static requests.UntappdAPIRequests.authenticate;
import static utils.restassured.RequestSpecificationUtils.createAuthenticatedRequestSpecification;
import static utils.restassured.RequestSpecificationUtils.createCommonRequestSpecification;
import static utils.restassured.RequestSpecificationUtils.createNotAuthenticatedRequestSpecification;

public class BaseAPITest {
    public static String environment;
    static RequestSpecification nonAuthenticatedRequestSpec;
    static RequestSpecification authenticatedRequestSpec;
    static RequestSpecification commonRequestSpec;

    @BeforeAll
    static void setUp() {
        environment = System.getProperty("environment");
        commonRequestSpec = createCommonRequestSpecification();
        nonAuthenticatedRequestSpec = createNotAuthenticatedRequestSpecification();
        authenticatedRequestSpec = createAuthenticatedRequestSpecification(getToken());
    }

    private static String getToken() {
        Properties testDataProps = TestDataUtils.getTestDataProperties();
        String username = testDataProps.getProperty("username");
        String password = testDataProps.getProperty("password");
        String appVersion = testDataProps.getProperty("app_version");
        String deviceName = testDataProps.getProperty("device_name");
        String devicePlatform = testDataProps.getProperty("device_platform");
        String deviceVersion = testDataProps.getProperty("device_version");
        String existingDevice = testDataProps.getProperty("existing_device");

        return authenticate(nonAuthenticatedRequestSpec, username, password, appVersion, deviceName, devicePlatform, deviceVersion, existingDevice).getBody().path("'response'.'access_token'");
    }
}
