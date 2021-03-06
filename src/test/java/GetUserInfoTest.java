import io.qameta.allure.Description;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Story;
import io.restassured.response.Response;
import org.testng.annotations.Test;
import java.util.List;
import static io.restassured.RestAssured.*;

public class GetUserInfoTest extends SetUp {

    @Epic("User-controller")
    @Feature("Get user info")
    @Story("Correct request")
    @Description(value = "Get user info by ID check")
    @Test
    public void getUserInfoByIdTest() {
        String id = response.jsonPath().getString("data.id");
        String avatar = response.jsonPath().getString("data.avatar");
        String email = response.jsonPath().getString("data.email");
        String name = response.jsonPath().getString("data.name");
        String role = response.jsonPath().getString("data.role");

        Response responseInfo =
                get(routes.getUser() + "/" + id)
                        .then().assertThat().spec(Specifications.checkStatusCode200AndContentType())
                        .extract().response();

        String avatarInfo = responseInfo.jsonPath().getString("data.avatar");
        String emailInfo = responseInfo.jsonPath().getString("data.email");
        String idInfo = responseInfo.jsonPath().getString("data.id");
        String nameInfo = responseInfo.jsonPath().getString("data.name");
        String roleInfo = responseInfo.jsonPath().getString("data.role");
        int customStatusCodeInfo = responseInfo.jsonPath().getInt("statusCode");
        String successInfo = responseInfo.jsonPath().getString("success");

        softAssert.assertEquals(avatarInfo,avatar, "Wrong \"avatar\"");
        softAssert.assertEquals(emailInfo,email, "Wrong \"email\"");
        softAssert.assertEquals(nameInfo,name, "Wrong \"name\"");
        softAssert.assertEquals(idInfo,id, "Wrong \"id\"");
        softAssert.assertEquals(roleInfo,role, "Wrong \"role\"");
        softAssert.assertEquals(customStatusCodeInfo,1, "Wrong \"statusCode\"");
        softAssert.assertEquals(successInfo,"true", "Wrong \"success\"");
        softAssert.assertAll();
    }

    @Epic("User-controller")
    @Feature("Get users")
    @Story("Correct request")
    @Description(value = "Get users check")
    @Test
    public void getUsers() {
        Response response =given()
                .header("Authorization", token)
                .get(routes.getUser())
                .then().assertThat().spec(Specifications.checkStatusCode200AndContentType())
                .extract().response();

        String success = response.jsonPath().getString("success");
        int customStatusCode = response.jsonPath().getInt("statusCode");
        List<Object> users = response.jsonPath().getList("data");

        softAssert.assertEquals(customStatusCode,1, "Wrong \"statusCode\"");
        softAssert.assertEquals(success,"true", "Wrong \"success\"");
        softAssert.assertNotNull(users, "\"data\" is null");
        softAssert.assertAll();
    }
}
