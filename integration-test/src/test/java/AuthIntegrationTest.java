import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.notNullValue;

public class AuthIntegrationTest {

    @BeforeAll
    static void setup() {
        RestAssured.baseURI = "http://localhost:8004";
    }

    @Test
    public void shouldReturnOKWithValidToken() {
        // 1. Arrange
        String loginPayload = """
                {
                    "email" : "testuser@test.com",
                    "password" : "password123"
                }
                """;

        // 2. act
        Response response = given()
                .contentType("application/json")
                .body(loginPayload)
                .when()
                .post("/auth/api/login")
                // 3. Assert
                .then()
                .statusCode(200)
                .body("token" , notNullValue())
                .extract().response();

        System.out.println("Generated token: " + response.jsonPath().getString("token"));
    }


    @Test
    public void shouldReturnUnauthorizedOnInvalidLogin() {
        // 1. Arrange
        String loginPayload = """
                {
                    "email" : "invalid_user@test.com",
                    "password" : "wrong_password"
                }
                """;

        // 2. act
        given()
                .contentType("application/json")
                .body(loginPayload)
                .when()
                .post("/auth/api/login")
                // 3. Assert
                .then()
                .statusCode(401);


        System.out.println("Provided Email and password is invalid as got 401 UNAUTHORIZED response.");
    }
}
