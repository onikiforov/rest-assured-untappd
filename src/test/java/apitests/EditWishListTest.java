package apitests;

import io.restassured.response.Response;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import requests.UntappdAPIRequests;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalToIgnoringCase;
import static org.hamcrest.Matchers.is;
import static utils.TestDataUtils.assertErrorTypeInvalidParam;
import static utils.TestDataUtils.assertSuccessfulResultByPath;
import static utils.TestDataUtils.createAddListForm;
import static utils.TestDataUtils.createUserList;
import static utils.TestDataUtils.deleteUserList;
import static utils.TestDataUtils.getTestDataProperties;

class EditWishListTest extends BaseAPITest {
    private static List<Integer> createdLists = new ArrayList<>();
    private static Properties testData = getTestDataProperties();

    @AfterEach
    void methodTearDown() {
        if (createdLists.size() > 0) {
            for (int listId : createdLists) {
                deleteUserList(listId, authenticatedRequestSpec);
            }
        }
        createdLists.clear();
    }

    @Test
    void editValidListNameTest() {
        int listId = createUserList(testData.getProperty("wish_list_name"), testData.getProperty("wish_list_description"), authenticatedRequestSpec);

        createdLists.add(listId);

        String listName = "Edited list name";

        Map<String, Object> editedListData = createAddListForm(listName, testData.getProperty("wish_list_description"));

        Response response = UntappdAPIRequests.editWishList(authenticatedRequestSpec, listId, editedListData);

        response.then().statusCode(200);

        assertSuccessfulResultByPath(response, "response.results");

        assertThat(response.getBody().path("response.list_name"), is(equalToIgnoringCase(listName)));
    }

    @Test
    void editValidListDescriptionTest() {
        int listId = createUserList(testData.getProperty("wish_list_name"), testData.getProperty("wish_list_description"), authenticatedRequestSpec);

        createdLists.add(listId);

        String listDescription = "Edited list description";

        Map<String, Object> editedListData = createAddListForm(testData.getProperty("wish_list_name"), listDescription);

        Response response = UntappdAPIRequests.editWishList(authenticatedRequestSpec, listId, editedListData);

        response.then().statusCode(200);

        assertSuccessfulResultByPath(response, "response.results");
    }

    @Test
    void editWishListInvalidId() {
        int listId = 123;

        String listDescription = "Edited list description";

        Map<String, Object> editedListData = createAddListForm(testData.getProperty("wish_list_name"), listDescription);

        Response response = UntappdAPIRequests.editWishList(authenticatedRequestSpec, listId, editedListData);

        response.then().statusCode(500);

        assertErrorTypeInvalidParam(response);
    }

    @Test
    void editWishListWithoutToken() {
        int listId = 123;

        String listDescription = "Edited list description";

        Map<String, Object> editedListData = createAddListForm(testData.getProperty("wish_list_name"), listDescription);

        Response response = UntappdAPIRequests.editWishList(commonRequestSpec, listId, editedListData);

        response.then().statusCode(500);

        assertErrorTypeInvalidParam(response);
    }
}
