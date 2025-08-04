package user;

import base.BaseTest;
import io.qameta.allure.Step;
import io.restassured.response.Response;
import org.junit.Before;
import org.junit.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;

public class UpdateUserTest extends BaseTest {
    private String email = "upd" + System.currentTimeMillis() + "@mail.ru";
    private String password = "pwd";

    @Before
    public void setup() {
        token = register("{\"email\":\"" + email + "\",\"password\":\"" + password + "\",\"name\":\"Orig\"}")
                .path("accessToken");
    }

    @Test
    public void authorizedUpdate() {
        String body = "{\"name\":\"NewName\"}";
        Response resp = given()
                .header("Content-type", "application/json")
                .header("Authorization", token)
                .body(body)
                .patch("/api/auth/user");
        resp.then().statusCode(200).body("success", is(true)).body("user.name", equalTo("NewName"));
    }

    @Test
    public void unauthorizedUpdateFails() {
        given()
                .header("Content-type", "application/json")
                .body("{\"name\":\"NoAuth\"}")
                .patch("/api/auth/user")
                .then()
                .statusCode(401);
    }

    @Step("Register for update tests")
    private Response register(String body) {
        return given()
                .header("Content-type", "application/json")
                .body(body)
                .post("/api/auth/register");
    }
}