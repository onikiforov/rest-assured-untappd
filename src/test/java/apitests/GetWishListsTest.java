package apitests;

import io.restassured.response.Response;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.equalToIgnoringCase;
import static org.hamcrest.Matchers.is;
import static requests.UntappdAPIRequests.getWishLists;
import static utils.TestDataUtils.assertErrorTypeInvalidParam;
import static utils.TestDataUtils.createUserList;
import static utils.TestDataUtils.deleteUserList;
import static utils.TestDataUtils.getTestDataProperties;

class GetWishListsTest extends BaseAPITest {
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
    void getDefaultWishListTest() {
        Response response = getWishLists(authenticatedRequestSpec);

        response.then().statusCode(200);

        assertThat(((List) response.getBody().path("response.items")).size(), is(equalTo(1)));
        assertThat(response.getBody().path("response.items[0].list_name"), is(equalToIgnoringCase("Wish List")));
    }

    @Test
    void getCreatedWishList() {
        int listId = createUserList(testData.getProperty("wish_list_name"), testData.getProperty("wish_list_description"), authenticatedRequestSpec);

        createdLists.add(listId);

        Response response = getWishLists(authenticatedRequestSpec);

        response.then().statusCode(200);
        assertThat(((List) response.getBody().path("response.items")).size(), is(equalTo(2)));

        List<String> listNames = response.getBody().path("response.items.list_name");

        assertThat(listNames, is(Matchers.hasItem(testData.getProperty("wish_list_name"))));
    }

    @Test
    void getWishListWithoutToken() {
        Response response = getWishLists(commonRequestSpec);

        response.then().statusCode(500);

        assertErrorTypeInvalidParam(response);
    }
}
