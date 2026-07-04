import Utils.AuthUtil;
import Utils.TestData;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.notNullValue;

public class PatientIntegrationTest {

    @BeforeAll
    public static void setUp() {
        RestAssured.baseURI = "http://localhost:8004";
    }

    @Test
    public void shouldReturnPatientWithValidToken() {
        // 1. Arrange
        String token = AuthUtil.getAuthToken();

       Response response = given()
               .header("Authorization", "Bearer " + token)
               .when()
               .get("/api/patients");
       response.then()
               .statusCode(200)
               .body("patients", notNullValue());

        System.out.println("\n----- Patient Response -----");
        response.prettyPrint();
        System.out.println("========== Test Passed ==========");
    }

    @Test
    public void shouldCreatePatientWithValidToken() {
        String token = AuthUtil.getAuthToken();

        Response response = given()
                .header("Authorization" , "Bearer "+ token)
                .contentType("application/json")
                .body(TestData.createPatientPayload())
                .when()
                .post("/api/patients");

        response.then()
                .statusCode(201);

        System.out.println("Created Patient:");
        response.prettyPrint();
    }

    @Test
    public void shouldReturnBadRequestWithSameExistedEmail() {
        String token = AuthUtil.getAuthToken();

        Response response = given()
                .header("Authorization", "Bearer "+ token)
                .contentType("application/json")
                .body(TestData.createPatientPayload())
                .when()
                .post("/api/patients");
        response.then()
                .statusCode(400);

        response.prettyPrint();
    }

    @Test
    public void shouldUpdatePatientWithValidToken() {
        String token = AuthUtil.getAuthToken();
        String patientId = "25bb3d31-4045-4897-9468-97bfa44c9939";

        Response response = given()
                .header("Authorization", "Bearer "+ token)
                .contentType("application/json")
                .body(TestData.updatePatientPayload())
                .when()
                .put("/api/patients/" + patientId);

        response.then()
                .statusCode(200);

        System.out.println("Updated Patient:");
        response.prettyPrint();
    }

    @Test
    public void updateShouldReturnBadRequestWithInvalidIdWithValidToken() {
        String token = AuthUtil.getAuthToken();
        String patientId = "25bb3d31-97bfa44c9939";

        Response response = given()
                .header("Authorization", "Bearer "+ token)
                .contentType("application/json")
                .body(TestData.updatePatientPayload())
                .when()
                .put("/api/patients/" + patientId);

        response.then()
                .statusCode(400);

        response.prettyPrint();
    }
}
