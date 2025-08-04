package order;

import base.BaseTest;
import io.qameta.allure.Step;
import io.restassured.response.Response;
import org.junit.Before;
import org.junit.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.notNullValue;

public class OrderListTest extends BaseTest {

    @Before
    public void setup() {
        token = registerUser("list" + System.currentTimeMillis() + "@mail.ru", "pw", "L")
                .path("accessToken");
    }

    @Test
    public void getOrdersAuthorized() {
        getOrders(token)
                .then().statusCode(200).body("orders", notNullValue());
    }

    @Test
    public void getOrdersUnauthorized() {
        getOrders(null)
                .then().statusCode(401);
    }

    @Step("Register user for order list tests")
    private Response registerUser(String email, String password, String name) {
        String json = String.format("{\"email\":\"%s\",\"password\":\"%s\",\"name\":\"%s\"}", email, password, name);
        return given()
                .header("Content-Type", "application/json")
                .body(json)
                .post("/api/auth/register");
    }

    @Step("Get orders list")
    private Response getOrders(String token) {
        if (token != null) {
            return given().header("Authorization", token).get("/api/orders");
        } else {
            return given().get("/api/orders");
        }
    }
}