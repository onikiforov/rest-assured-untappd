package apitests;

import io.restassured.response.Response;
import io.restassured.response.ResponseBody;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import requests.UntappdAPIRequests;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.stream.Stream;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static utils.TestDataUtils.assertErrorType;
import static utils.TestDataUtils.assertErrorTypeInvalidParam;
import static utils.TestDataUtils.assertSuccessfulResult;
import static utils.TestDataUtils.createAddCheckInForm;
import static utils.TestDataUtils.createAddCheckInFormNoBeerId;
import static utils.TestDataUtils.createAddCheckInFormNoOffset;
import static utils.TestDataUtils.deleteUserCheckIn;
import static utils.TestDataUtils.getTestDataProperties;

class AddCheckInTest extends BaseAPITest {
    private static List<Integer> createdCheckIns = new ArrayList<>();
    private static Properties testData = getTestDataProperties();

    private static int beerId;
    private static double rating;
    private static int gmtOffset;
    private static String timezone;

    @BeforeAll
    static void classSetUp() {
    // Get default data for new check in
        beerId = Integer.parseInt(testData.getProperty("beer_id"));
        rating = Double.parseDouble(testData.getProperty("rating"));
        gmtOffset = Integer.parseInt(testData.getProperty("gmt_offset"));
        timezone = testData.getProperty("timezone");
    }

    @AfterAll
    static void classTearDown() {
        // If we successfully created check ins, delete them during tear down phase
        if (createdCheckIns.size() > 0) {
            for (int listId : createdCheckIns) {
                deleteUserCheckIn(listId, authenticatedRequestSpec);
            }
        }
        createdCheckIns.clear();
    }

    private static Stream<Arguments> provideDataForAddNewCheckInMissingRequiredDataTest() {
        return Stream.of(
                Arguments.of(0, rating, gmtOffset, timezone, 500, "invalid_param", "Missing beer id"),
                Arguments.of(beerId, rating, 0, timezone, 500, "invalid_param", "Missing GMT offset"),
                Arguments.of(beerId, rating, gmtOffset, null, 500, "invalid_param", "Missing timezone")
        );
    }

    @Test
    void addNewCheckInValidTest() {
        // Create params for new check in
        Map<String, Object> requestParams = createAddCheckInForm(beerId, rating, gmtOffset, timezone);
        // Send check in request and get Response object
        Response response = UntappdAPIRequests.createCheckIn(authenticatedRequestSpec, requestParams);
        // Assert for status code
        response.then().statusCode(200);
        // Get response body
        ResponseBody responseBody = response.getBody();
        // Assert that response is successful
        assertSuccessfulResult(response);
        // Add id of created check in to the list to delete during tear down
        createdCheckIns.add(responseBody.path("response.checkin_id"));
        // Get rating score from response
        int responseRatingScore = responseBody.path("response.rating_score");
        // Convert to double
        double responseRatingScoreDouble = (double) responseRatingScore;
        // Assert that rating from response is the same as provided in check in request
        assertThat(responseRatingScoreDouble, is(equalTo(rating)));
        // Assert that beer id from response is the same as provided in check in request
        assertThat(responseBody.path("response.beer.bid"), is(equalTo(beerId)));
    }

    @ParameterizedTest(name = "{index} addNewCheckInMissingRequiredDataTest: {6}. BeerId: {0}; Rating: {1}; GmtOffset: {2}; Timezone: {3}. Expected status code: {4}. Expected error type: {5}")
    @MethodSource("provideDataForAddNewCheckInMissingRequiredDataTest")
    void addNewCheckInMissingRequiredDataTest(int beerId, double rating, int gmtOffset, String timezone, int statusCode, String errorType, String description) {
        Map<String, Object> requestParams;

        if (beerId == 0) {
            requestParams = createAddCheckInFormNoBeerId(gmtOffset, rating, timezone);
        } else if (gmtOffset == 0) {
            requestParams = createAddCheckInFormNoOffset(beerId, rating, timezone);
        } else {
            requestParams = createAddCheckInForm(beerId, rating, gmtOffset, timezone);
        }


        Response response = UntappdAPIRequests.createCheckIn(authenticatedRequestSpec, requestParams);

        response.then().statusCode(statusCode);

        assertErrorType(response, errorType);
    }

    @Test
    void addNewCheckInWithoutTokenTest() {
        Map<String, Object> requestParams = createAddCheckInForm(beerId, rating, gmtOffset, timezone);

        Response response = UntappdAPIRequests.createCheckIn(commonRequestSpec, requestParams);

        response.then().statusCode(500);

        assertErrorTypeInvalidParam(response);
    }
}
