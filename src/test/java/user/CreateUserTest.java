package user;

import base.BaseTest;
import io.qameta.allure.Step;
import io.restassured.response.Response;
import org.junit.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.is;

public class CreateUserTest extends BaseTest {

    @Test
    public void createUniqueUser() {
        String json = "{\"email\":\"user" + System.currentTimeMillis() + "@mail.ru\",\"password\":\"qwerty\",\"name\":\"TestUser\"}";
        Response resp = register(json);
        resp.then().statusCode(200).body("success", is(true));
        token = resp.path("accessToken");
    }

    @Test
    public void duplicateUserFails() {
        String json = "{\"email\":\"user" + System.currentTimeMillis() + "@mail.ru\",\"password\":\"qwerty\",\"name\":\"Dupe\"}";
        register(json).then().statusCode(200);
        register(json).then().statusCode(403).body("message", containsString("User already exists"));
    }

    @Test
    public void missingFieldFails() {
        String json = "{\"email\":\"nofield@mail.ru\",\"password\":\"\"}";
        register(json).then().statusCode(403);
    }

    @Step("Register user")
    private Response register(String body) {
        return given()
                .header("Content-Type", "application/json")
                .body(body)
                .post("/api/auth/register");
    }
}