import io.qameta.allure.Description;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Story;
import io.restassured.response.Response;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;
import java.util.List;
import static io.restassured.RestAssured.*;

public class WrongGetUserInfoTest {
    SoftAssert softAssert = new SoftAssert();
    Routes routes = new Routes();
    ErrorCode errorCode = new ErrorCode();
    String wrongUserId = Methods.generateRandomHexString(8) + "-" +
            Methods.generateRandomHexString(4) + "-" +
            Methods.generateRandomHexString(4) + "-" +
            Methods.generateRandomHexString(4) + "-" +
            Methods.generateRandomHexString(12);

    @Epic("User-controller")
    @Feature("Get user info")
    @Story("With wrong user ID")
    @Description(value = "Checking the correct server response")
    @Test
    public void withWrongUserId() {
        Response response = get(routes.getUser() + "/" + wrongUserId)
                .then().assertThat().spec(Specifications.checkStatusCode400AndContentType())
                .extract().response();
        String success = response.jsonPath().getString("success");
        int customStatusCode = response.jsonPath().getInt("statusCode");
        List<Integer> codes = response.jsonPath().getList("codes");

        softAssert.assertEquals(success,"true","Wrong \"success\"");
        softAssert.assertTrue(codes.contains(errorCode.USER_NOT_FOUND), "\"codes\" does not contain correct error code");
        softAssert.assertEquals(customStatusCode,codes.get(0).intValue(),"Wrong \"statusCode\"");
        softAssert.assertAll();
    }
}
