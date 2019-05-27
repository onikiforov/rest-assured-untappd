package apitests;

import io.restassured.response.Response;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import requests.UntappdAPIRequests;

import java.util.Properties;

import static utils.TestDataUtils.assertErrorTypeInvalidParam;
import static utils.TestDataUtils.assertSuccessfulResult;
import static utils.TestDataUtils.createCheckIn;
import static utils.TestDataUtils.getTestDataProperties;

class DeleteCheckIn extends BaseAPITest {
    private static Properties testData = getTestDataProperties();

    private static int beerId;
    private static double rating;
    private static int gmtOffset;
    private static String timezone;

    @BeforeAll
    static void classSetUp() {
        beerId = Integer.parseInt(testData.getProperty("beer_id"));
        rating = Double.parseDouble(testData.getProperty("rating"));
        gmtOffset = Integer.parseInt(testData.getProperty("gmt_offset"));
        timezone = testData.getProperty("timezone");
    }

    @Test
    void deleteCheckInTest() {
        int checkInId = createCheckIn(beerId, rating, gmtOffset, timezone, authenticatedRequestSpec);

        Response response = UntappdAPIRequests.deleteCheckIn(authenticatedRequestSpec, checkInId);

        response.then().statusCode(200);

        assertSuccessfulResult(response);
    }

    @Test
    void deleteCheckInWithNotValidIdTest() {
        int checkInId = 123;

        Response response = UntappdAPIRequests.deleteCheckIn(authenticatedRequestSpec, checkInId);

        response.then().statusCode(500);

        assertErrorTypeInvalidParam(response);
    }

    @Test
    void deleteCheckInWithoutToken() {
        int checkInId = 123;

        Response response = UntappdAPIRequests.deleteCheckIn(commonRequestSpec, checkInId);

        response.then().statusCode(500);

        assertErrorTypeInvalidParam(response);
    }
}
