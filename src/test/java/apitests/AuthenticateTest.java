package apitests;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import model.response.AuthResponseBody;
import org.json.JSONObject;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import utils.TestDataUtils;

import java.util.Properties;
import java.util.stream.Stream;

import static config.Endpoints.AUTH;
import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.lessThan;
import static requests.UntappdAPIRequests.authenticate;
import static utils.TestDataUtils.assertErrorType;

class AuthenticateTest extends BaseAPITest {
    private static Properties testDataProps = TestDataUtils.getTestDataProperties();

    private static String username;
    private static String password;
    private static String appVersion;
    private static String deviceName;
    private static String devicePlatform;
    private static String deviceVersion;
    private static String existingDevice;

    @BeforeAll
    static void classSetUp() {
        username = testDataProps.getProperty("username");
        password = testDataProps.getProperty("password");
        appVersion = testDataProps.getProperty("app_version");
        deviceName = testDataProps.getProperty("device_name");
        devicePlatform = testDataProps.getProperty("device_platform");
        deviceVersion = testDataProps.getProperty("device_version");
        existingDevice = testDataProps.getProperty("existing_device");
    }

    private static Stream<Arguments> provideDataForAuthenticateNegativeTests() {
        return Stream.of(
                Arguments.of("test@mail.com", "somedummypassword", 500, "invalid_param", "Email instead of username"),
                Arguments.of(testDataProps.getProperty("username"), "", 400, "invalid_param", "No password"),
                Arguments.of("", "123456", 400, "invalid_param", "No username"),
                Arguments.of(testDataProps.getProperty("username"), "somedummypassword", 403, "invalid_login", "Invalid password"),
                Arguments.of("somedummyuser", "somedummypassword", 403, "invalid_login", "User not found")
        );
    }

    @ParameterizedTest(name = "{index} authenticateNegativeWithParametrizedDataTest: {4}. Username: {0}; Password: {1}. Expected status code: {2}. Expected error type: {3}")
    @MethodSource("provideDataForAuthenticateNegativeTests")
    void authenticateNegativeWithParametrizedDataTests(String username, String password, int statusCode, String errorType, String description) {
        Response response = authenticate(nonAuthenticatedRequestSpec, username, password, appVersion, deviceName, devicePlatform, deviceVersion, existingDevice);

        response.then().statusCode(statusCode);
        assertErrorType(response, errorType);
    }

    @Test
    void authenticateWithValidCredsTestAssertStatusCode() {
        // Send request and get Response object
        Response response = authenticate(nonAuthenticatedRequestSpec, username, password, appVersion, deviceName, devicePlatform, deviceVersion, existingDevice);
        // Assert that status code is 200
        response.then().statusCode(200);
    }

    @Test
    void authenticateWithValidCredsTestAssertResponseBodyByClass() {
        Response response = authenticate(nonAuthenticatedRequestSpec, username, password, appVersion, deviceName, devicePlatform, deviceVersion, existingDevice);
        response.then().statusCode(200);

        AuthResponseBody responseBody = response.as(AuthResponseBody.class);

        assertThat(responseBody.getResponse().getAccessToken(), is(notNullValue()));
    }

    @Test
    void authenticateWithValidCredsTestAssertTokenByJsonPath() {
        Response response = authenticate(nonAuthenticatedRequestSpec, username, password, appVersion, deviceName, devicePlatform, deviceVersion, existingDevice);
        response.then().statusCode(200);

        /* Response JSON is:
        {
            "meta": {
                "code": 200,
                "response_time": {
                    "time": 0.076,
                    "measure": "seconds"
                },
                "init_time": {
                    "time": 0,
                    "measure": "seconds"
                }
            },
            "notifications": [
        
            ],
            "response": {
                "two_factor_enabled": 0,
                "access_token": "someTokenString",
                "default_load": false
            }
        }
         */

        assertThat(response.body().path("response.access_token"), is(notNullValue()));
    }

    @Test
    void authenticateWithValidCredsTestAssertResponseBodyByJsonSchema() {
        Response response = authenticate(nonAuthenticatedRequestSpec, username, password, appVersion, deviceName, devicePlatform, deviceVersion, existingDevice);
        response.then().statusCode(200);

        response.then().assertThat().body(matchesJsonSchemaInClasspath("Authenticate.json"));
    }

    @Test
    void authJsonBodyTest() {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("user_name", username);
        jsonObject.put("user_password", password);

        Response response = RestAssured.given().spec(nonAuthenticatedRequestSpec).body(jsonObject.toString()).post(AUTH);

        response.then().statusCode(400);
    }

    @Test
    void authenticateWithValidCredsTestAssertRequestTime() {
        Response response = authenticate(nonAuthenticatedRequestSpec, username, password, appVersion, deviceName, devicePlatform, deviceVersion, existingDevice);
        response.then().statusCode(200)
                .time(lessThan(2000L));

    }


    @Test
    void authenticateWithValidCredsTestAssertAll() {
        Response response = authenticate(nonAuthenticatedRequestSpec, username, password, appVersion, deviceName, devicePlatform, deviceVersion, existingDevice);
        response.then().statusCode(200);
        response.then().time(lessThan(30000L));

        response.then().assertThat().body(matchesJsonSchemaInClasspath("Authenticate.json"));
    }
}
