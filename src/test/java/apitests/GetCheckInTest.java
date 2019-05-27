package apitests;

import io.restassured.response.Response;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Test;
import requests.UntappdAPIRequests;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static utils.TestDataUtils.assertErrorTypeInvalidParam;
import static utils.TestDataUtils.createCheckIn;
import static utils.TestDataUtils.deleteUserCheckIn;
import static utils.TestDataUtils.getTestDataProperties;

class GetCheckInTest extends BaseAPITest {
    private static List<Integer> createdCheckIns = new ArrayList<>();
    private static Properties testData = getTestDataProperties();

    @AfterAll
    static void classTearDown() {
        if (createdCheckIns.size() > 0) {
            for (int listId : createdCheckIns) {
                deleteUserCheckIn(listId, authenticatedRequestSpec);
            }
        }
        createdCheckIns.clear();
    }

    @Test
    void getCreatedCheckInTest() {
        int beerId = Integer.parseInt(testData.getProperty("beer_id"));
        double rating = Double.parseDouble(testData.getProperty("rating"));
        int gmtOffset = Integer.parseInt(testData.getProperty("gmt_offset"));
        String timezone = testData.getProperty("timezone");

        int checkInId = createCheckIn(beerId, rating, gmtOffset, timezone, authenticatedRequestSpec);

        createdCheckIns.add(checkInId);

        Response response = UntappdAPIRequests.getCheckIn(authenticatedRequestSpec, checkInId);

        response.then().statusCode(200);

        assertThat(response.getBody().path("response.checkin.checkin_id"), is(equalTo(checkInId)));
    }

    @Test
    void getCheckInNotInvalidId() {
        int checkInId = 123;

        Response response = UntappdAPIRequests.getCheckIn(authenticatedRequestSpec, checkInId);

        response.then().statusCode(404);

        assertErrorTypeInvalidParam(response);
    }

    @Test
    void getCheckInWithoutToken() {
        int checkInId = 123;

        Response response = UntappdAPIRequests.getCheckIn(commonRequestSpec, checkInId);

        response.then().statusCode(500);

        assertErrorTypeInvalidParam(response);
    }
}
