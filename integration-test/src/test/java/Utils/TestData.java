package Utils;

public class TestData {

    public static String createPatientPayload() {
        return """
                {
                    "name": "Souvik mondal",
                    "email": "Souvik.mondal@gmail.com",
                    "address": "123 Bankura West Bengal",
                    "dateOfBirth": "1980-01-01",
                    "registeredDate": "2026-06-21"
                }
                """;
    }

    public static String updatePatientPayload() {
        return """
                {
                  "name": "Pritam mondal Updated",
                  "email": "pritam.mondal.updated@gmail.com",
                  "address": "Kolkata",
                  "dateOfBirth": "2003-09-16",
                  "registeredDate": "2026-06-21"
                }
                """;
    }
}
