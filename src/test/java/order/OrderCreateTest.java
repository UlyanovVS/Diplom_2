package order;

import base.BaseTest;
import io.qameta.allure.Step;
import io.restassured.response.Response;
import org.junit.Before;
import org.junit.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;

public class OrderCreateTest extends BaseTest {

    @Before
    public void setup() {
        token = given()
                .header("Content-Type", "application/json")
                .body("{\"email\":\"ord" + System.currentTimeMillis() + "@mail.ru\",\"password\":\"pw\",\"name\":\"O\"}")
                .post("/api/auth/register")
                .path("accessToken");
    }

    @Test
    public void createOrderWithAuth() {
        Response resp = createOrder(token, "[\"61c0c5a71d1f82001bdaaa6c\"]");
        resp.then().statusCode(200).body("success", is(true)).body("order.number", notNullValue());
    }

    @Test
    public void createOrderWithoutAuthFails() {
        createOrder(null, "[\"61c0c5a71d1f82001bdaaa6c\"]")
                .then().statusCode(401);
    }

    @Test
    public void createOrderNoIngredientsFails() {
        createOrder(token, "[]").then().statusCode(400);
    }

    @Test
    public void createOrderInvalidIngredientHash() {
        createOrder(token, "[\"invalidhash\"]").then().statusCode(500);
    }

    @Step("Create order")
    private Response createOrder(String token, String jsonArray) {
        return given()
                .header("Content-type", "application/json")
                .header(token != null ? "Authorization" : "", token != null ? token : "")
                .body("{\"ingredients\":" + jsonArray + "}")
                .post("/api/orders");
    }
}