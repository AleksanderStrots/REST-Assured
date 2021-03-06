import io.qameta.allure.Description;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Story;
import io.restassured.response.Response;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;
import java.util.List;
import static io.restassured.RestAssured.*;

public class DeleteUserTest {

    String correctEmail = Methods.generateRandomHexString(5) + "@gmail.com";
    String correctPassword = Methods.generateRandomHexString(5);
    String avatarPath = "src/main/resources/avatar.jpeg";
    String correctName = Methods.generateRandomHexString(5);
    String correctRole = "user";
    String emptyToken = "";

    ErrorCode errorCode = new ErrorCode();
    Methods methods = new Methods();
    Routes routes = new Routes();

    @Epic("User-controller")
    @Feature("Delete user")
    @Story("Correct request")
    @Description(value = "Checking user deletion")
    @Test
    public void deleteUser() {
        SoftAssert softAssert = new SoftAssert();
        Register user = new Register(avatarPath, correctEmail, correctName, correctPassword, correctRole);
        Response response = methods.registration(user);

        String token = response.jsonPath().getString("data.token");
        Response responseDelete = methods.deleteUser(token);
        int customStatusCode = responseDelete.jsonPath().getInt("statusCode");
        String success = responseDelete.jsonPath().getString("success");

        softAssert.assertEquals(success, "true", "Wrong \"success\"");
        softAssert.assertEquals(customStatusCode, 1, "Wrong \"statusCode\"");
        softAssert.assertAll();
    }

    @Epic("User-controller")
    @Feature("Delete user")
    @Story("With empty token")
    @Description(value = "Checking correct response")
    @Test
    public void deleteWithEmptyToken() {
        SoftAssert softAssert = new SoftAssert();
        Register user = new Register(avatarPath, correctEmail, correctName, correctPassword, correctRole);
        String token = methods.registration(user).jsonPath().getString("data.token");

        Response responseDelete = given()
                .header("Authorization", emptyToken)
                .delete(routes.getUser())
                .then().assertThat().spec(Specifications.checkStatusCode401AndContentType()).extract().response();

        int customStatusCode = responseDelete.jsonPath().getInt("statusCode");
        String success = responseDelete.jsonPath().getString("success");
        List<Integer> codes = responseDelete.jsonPath().getList("codes");

        softAssert.assertEquals(success, "true", "Wrong \"success\"");
        softAssert.assertTrue(codes.contains(errorCode.UNAUTHORIZED), "\"codes\" does not contain correct error code");
        softAssert.assertEquals(customStatusCode, codes.get(0).intValue(), "Wrong \"statusCode\"");
        softAssert.assertAll();
        methods.deleteUser(token);
    }

    @Epic("User-controller")
    @Feature("Delete user")
    @Story("Without token")
    @Description(value = "Checking correct response")
    @Test
    public void deleteWithoutToken() {
        SoftAssert softAssert = new SoftAssert();
        Register user = new Register(avatarPath, correctEmail, correctName, correctPassword, correctRole);
        String token = methods.registration(user).jsonPath().getString("data.token");

        Response responseDelete =
                delete(routes.getUser())
                        .then().assertThat().spec(Specifications.checkStatusCode401AndContentType()).extract().response();

        int customStatusCode = responseDelete.jsonPath().getInt("statusCode");
        String success = responseDelete.jsonPath().getString("success");
        List<Integer> codes = responseDelete.jsonPath().getList("codes");

        softAssert.assertEquals(success, "true", "Wrong \"success\"");
        softAssert.assertTrue(codes.contains(errorCode.UNAUTHORIZED), "\"codes\" does not contain correct error code");
        softAssert.assertEquals(customStatusCode, codes.get(0).intValue(), "Wrong \"statusCode\"");
        softAssert.assertAll();
        methods.deleteUser(token);
    }
}
