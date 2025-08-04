package user;

import base.BaseTest;
import io.qameta.allure.Step;
import io.restassured.response.Response;
import org.junit.Before;
import org.junit.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.notNullValue;

public class LoginUserTest extends BaseTest {
    private String email = "login" + System.currentTimeMillis() + "@mail.ru";
    private String password = "pass123";

    @Before
    public void setup() {
        register("{\"email\":\"" + email + "\",\"password\":\"" + password + "\",\"name\":\"LoginUser\"}");
    }

    @Test
    public void loginValidUser() {
        Response resp = login("{\"email\":\"" + email + "\",\"password\":\"" + password + "\"}");
        resp.then().statusCode(200).body("accessToken", notNullValue());
        token = resp.path("accessToken");
    }

    @Test
    public void loginInvalidCredentials() {
        login("{\"email\":\"bad@mail.ru\",\"password\":\"wrong\"}")
                .then().statusCode(401);
    }

    @Step("Register user")
    private Response register(String body) {
        return given()
                .header("Content-type", "application/json")
                .body(body)
                .post("/api/auth/register");
    }

    @Step("Login user")
    private Response login(String body) {
        return given()
                .header("Content-type", "application/json")
                .body(body)
                .post("/api/auth/login");
    }
}