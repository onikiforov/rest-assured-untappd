package apitests;

import io.restassured.response.Response;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import requests.UntappdAPIRequests;
import utils.TestDataUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalToIgnoringCase;
import static org.hamcrest.Matchers.is;
import static utils.TestDataUtils.assertErrorTypeInvalidParam;
import static utils.TestDataUtils.assertSuccessfulResultByPath;
import static utils.TestDataUtils.deleteUserList;
import static utils.TestDataUtils.getTestDataProperties;

class CreateWishListTest extends BaseAPITest {
    private static List<Integer> createdLists = new ArrayList<>();
    private static Properties testData = getTestDataProperties();

    private static String wishListName;
    private static String wishListDescription;

    @BeforeAll
    static void classSetUp() {
        wishListName = testData.getProperty("wish_list_name");
        wishListDescription = testData.getProperty("wish_list_description");
    }

    @AfterAll
    static void classTearDown() {
        if (createdLists.size() > 0) {
            for (int listId : createdLists) {
                deleteUserList(listId, authenticatedRequestSpec);
            }
        }
        createdLists.clear();
    }

    @Test
    void createWishListValidData() {
        Map<String, Object> requestParams = TestDataUtils.createAddListForm(wishListName, wishListDescription);
        Response response = UntappdAPIRequests.createWishList(authenticatedRequestSpec, requestParams);

        response.then().statusCode(200);

        assertSuccessfulResultByPath(response, "response.results");
        assertThat(response.getBody().path("response.list_name"), is(equalToIgnoringCase(wishListName)));

        createdLists.add(response.getBody().path("response.list_id"));
    }

    @Test
    void createWishListEmptyName() {
        Map<String, Object> requestParams = TestDataUtils.createAddListForm("", wishListDescription);
        Response response = UntappdAPIRequests.createWishList(authenticatedRequestSpec, requestParams);

        response.then().statusCode(500);
        assertErrorTypeInvalidParam(response);
    }

    @Test
    void createWishListWithoutToken() {
        Map<String, Object> requestParams = TestDataUtils.createAddListForm(wishListName, wishListDescription);
        Response response = UntappdAPIRequests.createWishList(commonRequestSpec, requestParams);

        response.then().statusCode(500);

        assertErrorTypeInvalidParam(response);
    }
}
