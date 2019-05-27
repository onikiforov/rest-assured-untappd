package apitests;

import io.restassured.response.Response;
import org.junit.jupiter.api.Test;

import java.util.Properties;

import static requests.UntappdAPIRequests.deleteWishList;
import static utils.TestDataUtils.assertErrorTypeInvalidParam;
import static utils.TestDataUtils.createUserList;
import static utils.TestDataUtils.getTestDataProperties;

class DeleteWishListTest extends BaseAPITest {
    private static Properties testData = getTestDataProperties();

    @Test
    void deleteWishListTest() {
        int listId = createUserList(testData.getProperty("wish_list_name"), testData.getProperty("wish_list_description"), authenticatedRequestSpec);

        Response response = deleteWishList(authenticatedRequestSpec, listId);

        response.then().statusCode(200);


    }

    @Test
    void deleteWishListWithNotValidIdTest() {
        int listId = 123;

        Response response = deleteWishList(authenticatedRequestSpec, listId);

        response.then().statusCode(500);

        assertErrorTypeInvalidParam(response);
    }

    @Test
    void deleteWishListWithNoToken() {
        int listId = 123;

        Response response = deleteWishList(commonRequestSpec, listId);

        response.then().statusCode(500);

        assertErrorTypeInvalidParam(response);
    }
}
