package requests;

import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

import java.util.Map;

import static config.Endpoints.ADD;
import static config.Endpoints.AUTH;
import static config.Endpoints.CHECKIN;
import static config.Endpoints.CHECK_USERNAME;
import static config.Endpoints.CUSTOM_LISTS;
import static config.Endpoints.DELETE;
import static config.Endpoints.EDIT;
import static config.Endpoints.HELPERS;
import static config.Endpoints.LIST_COUNTRIES;
import static config.Endpoints.USER_LISTS;
import static config.Endpoints.VIEW;
import static io.restassured.RestAssured.given;

public class UntappdAPIRequests {
    public static Response authenticate(RequestSpecification requestSpecification, String username, String password, String appVersion, String deviceName, String devicePlatform, String deviceVersion, String existingDevice) {
        // Create Response object by invoking RestAssured.given() method
        return given()
                // add request specification (url, headers, other reusable things)
                .spec(requestSpecification)
                // Add content type header
                .contentType(ContentType.URLENC.withCharset("UTF-8"))
                // Add Form URL-Encoded parameters
                .formParam("app_verison", appVersion)
                .formParam("device_name", deviceName)
                .formParam("device_platform", devicePlatform)
                .formParam("device_version", deviceVersion)
                .formParam("existing_device", existingDevice)
                .formParam("user_name", username)
                .formParam("user_password", password)
                // Send POST request to provided endpoint
                .post(AUTH);
    }

    public static Response getListOfCountries(RequestSpecification requestSpecification) {
        return given()
                .spec(requestSpecification)
                .get(HELPERS + LIST_COUNTRIES);
    }

    public static Response checkUsername(RequestSpecification requestSpecification, String username) {
        return given()
                .spec(requestSpecification)
                .contentType(ContentType.URLENC.withCharset("UTF-8"))
                .formParam("user_name", username)
                .post(HELPERS + CHECK_USERNAME);
    }

    public static Response getWishLists(RequestSpecification requestSpecification) {
        return given()
                .spec(requestSpecification)
                .get(CUSTOM_LISTS + USER_LISTS);
    }

    public static Response createWishList(RequestSpecification requestSpecification, Map<String, ?> requestParams) {
        return given()
                .spec(requestSpecification)
                .formParams(requestParams)
                .post(CUSTOM_LISTS + ADD);
    }

    public static Response deleteWishList(RequestSpecification requestSpecification, int listId) {
        return given()
                .spec(requestSpecification)
                .post(CUSTOM_LISTS + DELETE + "/" + listId);
    }

    public static Response editWishList(RequestSpecification requestSpecification, int listId, Map<String, ?> requestParams) {
        return given()
                .spec(requestSpecification)
                .formParams(requestParams)
                .post(CUSTOM_LISTS + EDIT + "/" + listId);
    }

    public static Response createCheckIn(RequestSpecification requestSpecification, Map<String, ?> requestParams) {
        return given()
                .spec(requestSpecification)
                .formParams(requestParams)
                .post(CHECKIN + ADD);
    }

    public static Response deleteCheckIn(RequestSpecification requestSpecification, int checkInId) {
        return given()
                .spec(requestSpecification)
                .post(CHECKIN + DELETE + "/" + checkInId);
    }

    public static Response getCheckIn(RequestSpecification requestSpecification, int checkInId) {
        return given()
                .spec(requestSpecification)
                .pathParam("checkInId", checkInId) // Example of using pathParam
                .get(CHECKIN + VIEW + "/{checkInId}");
    }

    public static Response editCheckIn(RequestSpecification requestSpecification, int checkInId, Map<String, ?> requestParams) {
        return given()
                .spec(requestSpecification)
                .formParams(requestParams)
                .post(CHECKIN + EDIT + "/" + checkInId);
    }
}