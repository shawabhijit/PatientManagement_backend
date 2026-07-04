package Utils;

import static io.restassured.RestAssured.given;

public class AuthUtil {

    public static String getAuthToken() {
        String loginPayload = """
                {
                    "email":"testuser@test.com",
                    "password":"password123"
                }
                """;

        return given()
                .contentType("application/json")
                .body(loginPayload)
                .when()
                .post("/auth/api/login")
                .then()
                .statusCode(200)
                .extract()
                .jsonPath()
                .getString("token");
    }
}
