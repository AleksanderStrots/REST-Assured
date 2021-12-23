import io.restassured.response.Response;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.*;

public class DeletePostTest extends SetUp {
    String imagePath = "src/main/resources/postPicture.jpeg";
    String image = Methods.uploadFile(imagePath).jsonPath().getString("data");

    String description = Methods.generateRandomHexString(5);
    String[] tags = {Methods.generateRandomHexString(5)};
    String title = Methods.generateRandomHexString(5);
    Post newsDto = new Post(description, image, tags, title);

    SoftAssertions softAssertions = new SoftAssertions();

    @Test
    public void deletePostTest() {
        Methods.createPost(token, newsDto);
        String author = response.jsonPath().getString("data.name");
        Response responseAfterGetPost = Methods.getPost(author, description, 1, 1, tags);
        String postId = responseAfterGetPost.jsonPath().getString("content[0].id");
        Response responseAfterDeletePost = given()
                .header("Authorization", token)
                .when().delete(Routes.news + "/" + postId)
                .then().assertThat().spec(Specifications.checkStatusCode200AndContentType())
                .extract().response();
        String success = responseAfterDeletePost.jsonPath().getString("success");
        int customStatusCode = responseAfterDeletePost.jsonPath().getInt("statusCode");

        softAssertions.assertThat(success).isEqualTo("true");
        softAssertions.assertThat(customStatusCode).isEqualTo(1);
        softAssertions.assertAll();
    }
}
