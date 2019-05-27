package apitests;

import io.restassured.response.Response;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.List;
import java.util.stream.Stream;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalToIgnoringCase;
import static org.hamcrest.Matchers.is;
import static requests.UntappdAPIRequests.checkUsername;
import static requests.UntappdAPIRequests.getListOfCountries;
import static utils.DateUtils.getCurrentTimeStampSeconds;
import static utils.TestDataUtils.assertSuccessfulResult;

class HelpersTest extends BaseAPITest {
    private static Stream<Arguments> provideDataForCheckUsernameInvalidTests() {
        return Stream.of(
                Arguments.of("S", 400, "invalid_param", "Username from a single character"),
                Arguments.of("So", 400, "invalid_param", "Username already taken"),
                Arguments.of("So*&!&%!", 400, "invalid_param", "Username with special symbols"),
                Arguments.of("", 400, "invalid_param", "Empty username")
        );
    }

    @Test
    void getListOfCountriesTest() {
        Response response = getListOfCountries(nonAuthenticatedRequestSpec);

        response.then().statusCode(200);

        int count = response.getBody().path("response.count");
        int itemsLength = ((List) response.getBody().path("response.items")).size();

        assertThat(count, is(Matchers.equalTo(itemsLength)));
    }

    @Test
    void checkUsernameValidTest() {
        String username = "idoubtthisnameistaken" + getCurrentTimeStampSeconds();
        Response response = checkUsername(nonAuthenticatedRequestSpec, username);

        response.then().statusCode(200);
        assertSuccessfulResult(response);
    }

    @ParameterizedTest(name = "{index} checkUsernameInvalidTests: {3}. Username: {0}; Expected status code: {1}. Expected error type: {2}")
    @MethodSource("provideDataForCheckUsernameInvalidTests")
    void checkUsernameInvalidTests(String username, int statusCode, String errorType, String description) {
        Response response = checkUsername(nonAuthenticatedRequestSpec, username);

        response.then().statusCode(statusCode);
        assertThat(response.getBody().path("meta.error_type"), is(equalToIgnoringCase(errorType)));
    }
}
