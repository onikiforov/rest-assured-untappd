package utils;

import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import model.response.ErrorResponseBody;
import requests.UntappdAPIRequests;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalToIgnoringCase;
import static org.hamcrest.Matchers.is;
import static requests.UntappdAPIRequests.createWishList;
import static requests.UntappdAPIRequests.deleteCheckIn;
import static requests.UntappdAPIRequests.deleteWishList;
import static utils.PropertiesUtils.getTestDataFilename;
import static utils.PropertiesUtils.getTestDataPath;
import static utils.PropertiesUtils.loadProperties;

public class TestDataUtils {
    public static Properties getTestDataProperties() {
        return loadProperties(getTestDataPath() + getTestDataFilename());
    }

    public static Map<String, Object> createAddListForm(String listName, String listDescription) {
        Map<String, Object> requestParams = new HashMap<>();
        requestParams.put("edit_item_view", 1);
        requestParams.put("is_public", 0);
        requestParams.put("list_name", listName);
        requestParams.put("list_description", listDescription);

        return requestParams;
    }

    public static Map<String, Object> createAddCheckInForm(int beerId, double rating, int gmtOffset, String timezone) {
        Map<String, Object> requestParams = new HashMap<>();
        requestParams.put("bid", beerId);
        requestParams.put("gmt_offset", gmtOffset);
        requestParams.put("rating", rating);
        requestParams.put("timezone", timezone);

        return requestParams;
    }

    public static Map<String, Object> createAddCheckInFormNoBeerId(int gmtOffset, double rating, String timezone) {
        Map<String, Object> requestParams = new HashMap<>();
        requestParams.put("gmt_offset", gmtOffset);
        requestParams.put("rating", rating);
        requestParams.put("timezone", timezone);

        return requestParams;
    }

    public static Map<String, Object> createAddCheckInFormNoOffset(int beerId, double rating, String timezone) {
        Map<String, Object> requestParams = new HashMap<>();
        requestParams.put("bid", beerId);
        requestParams.put("rating", rating);
        requestParams.put("timezone", timezone);

        return requestParams;
    }

    public static void deleteUserList(int listId, RequestSpecification requestSpecification) {
        deleteWishList(requestSpecification, listId);
    }

    public static int createUserList(String listName, String listDescription, RequestSpecification requestSpecification) {
        Map<String, Object> requestParams = TestDataUtils.createAddListForm(listName, listDescription);
        Response response = createWishList(requestSpecification, requestParams);
        return response.getBody().path("response.list_id");
    }

    public static void deleteUserCheckIn(int checkInId, RequestSpecification requestSpecification) {
        deleteCheckIn(requestSpecification, checkInId);
    }

    public static int createCheckIn(int beerId, double rating, int gmtOffset, String timezone, RequestSpecification requestSpecification) {
        Map<String, Object> requestParams = createAddCheckInForm(beerId, rating ,gmtOffset, timezone);
        Response response = UntappdAPIRequests.createCheckIn(requestSpecification, requestParams);
        return response.getBody().path("response.checkin_id");
    }

    public static void assertSuccessfulResult(Response response) {
        assertSuccessfulResultByPath(response, "response.result");
    }

    public static void assertSuccessfulResultByPath(Response response, String path) {
        assertThat(response.getBody().path(path), is(equalToIgnoringCase("success")));
    }

    public static void assertErrorType(Response response, String errorType) {
        assertThat(response.body().as(ErrorResponseBody.class).getMeta().getErrorType(), is(equalToIgnoringCase(errorType)));
    }

    public static void assertErrorTypeInvalidParam(Response response) {
        assertErrorType(response, "invalid_param");
    }
}
