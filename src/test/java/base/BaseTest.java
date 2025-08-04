package base;

import io.restassured.RestAssured;
import org.junit.After;
import org.junit.Before;

import static io.restassured.RestAssured.given;

public class BaseTest {
    protected String token;

    @Before
    public void setUp() {
        RestAssured.baseURI = "https://stellarburgers.nomoreparties.site";
    }

    @After
    public void cleanup() {
        if (token != null) {
            given().header("Authorization", token).delete("/api/auth/user");
        }
    }
}
