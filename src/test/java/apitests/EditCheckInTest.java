package apitests;

import io.restassured.response.Response;
import io.restassured.response.ResponseBody;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.equalToIgnoringCase;
import static org.hamcrest.Matchers.is;
import static requests.UntappdAPIRequests.editCheckIn;
import static utils.TestDataUtils.assertErrorTypeInvalidParam;
import static utils.TestDataUtils.createAddCheckInForm;
import static utils.TestDataUtils.createCheckIn;
import static utils.TestDataUtils.deleteUserCheckIn;
import static utils.TestDataUtils.getTestDataProperties;

class EditCheckInTest extends BaseAPITest {
    private static List<Integer> createdCheckIns = new ArrayList<>();
    private static Properties testData = getTestDataProperties();

    private static int beerId;
    private static double rating;
    private static int gmtOffset;
    private static String timezone;

    @AfterAll
    static void classTearDown() {
        if (createdCheckIns.size() > 0) {
            for (int listId : createdCheckIns) {
                deleteUserCheckIn(listId, authenticatedRequestSpec);
            }
        }
        createdCheckIns.clear();
    }

    @BeforeAll
    static void classSetUp() {
        beerId = Integer.parseInt(testData.getProperty("beer_id"));
        rating = Double.parseDouble(testData.getProperty("rating"));
        gmtOffset = Integer.parseInt(testData.getProperty("gmt_offset"));
        timezone = testData.getProperty("timezone");
    }

    @Test
    void editCheckInRatingTest() {
        int checkInId = createCheckIn(beerId, rating, gmtOffset, timezone, authenticatedRequestSpec);

        createdCheckIns.add(checkInId);

        double editedRating = 3.25;

        Map<String, Object> editedCheckInData = createAddCheckInForm(beerId, editedRating, gmtOffset, timezone);

        Response response = editCheckIn(authenticatedRequestSpec, checkInId, editedCheckInData);

        response.then().statusCode(200);

        ResponseBody responseBody = response.getBody();

        assertThat(responseBody.path("response.rating.response.result"), is(equalToIgnoringCase("success")));

        int responseOldRatingScore = responseBody.path("response.rating.response.old_score");
        double responseOldRatingScoreDouble = (double) responseOldRatingScore;
        assertThat(responseOldRatingScoreDouble, is(equalTo(rating)));

        float responseNewRatingScore = responseBody.path("response.rating.response.new_score");
        double responseNewRatingScoreDouble = (double) responseNewRatingScore;
        assertThat(responseNewRatingScoreDouble, is(equalTo(editedRating)));
    }

    @Test
    void editCheckInWithInvalidId() {
        int checkInId = 123;

        Map<String, Object> editedCheckInData = createAddCheckInForm(beerId, rating, gmtOffset, timezone);

        Response response = editCheckIn(authenticatedRequestSpec, checkInId, editedCheckInData);

        response.then().statusCode(500);

        assertErrorTypeInvalidParam(response);
    }

    @Test
    void editCheckInWithoutToken() {
        int checkInId = 123;

        Map<String, Object> editedCheckInData = createAddCheckInForm(beerId, rating, gmtOffset, timezone);

        Response response = editCheckIn(commonRequestSpec, checkInId, editedCheckInData);

        response.then().statusCode(500);

        assertErrorTypeInvalidParam(response);
    }
}
