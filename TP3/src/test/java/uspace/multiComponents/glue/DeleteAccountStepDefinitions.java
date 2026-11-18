package uspace.multiComponents.glue;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.cucumber.java.fr.Alors;
import io.cucumber.java.fr.Etantdonnéque;
import io.cucumber.java.fr.Quand;
import io.restassured.response.Response;
import uspace.UspaceMain;

import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class DeleteAccountStepDefinitions {

    private static final String JSON = "application/json";
    private static final String ACCOUNTS_PATH = "/accounts";
    private static final String CRUISES_PATH = "/cruises/";
    private static final String BOOKINGS_PATH = "/bookings";
    private static final String DEFAULT_CRUISE = UspaceMain.DEFAULT_CRUISE_ID;

    private static final int STATUS_CREATED = 201;

    private static final String CABIN_TYPE_STANDARD = "STANDARD";
    private static final String BOOKING_DATE_TIME = "2085-01-01T10:00";
    private static final String CATEGORY_ADULT = "ADULT";

    private static int sequence = 1;

    private final ObjectMapper mapper = new ObjectMapper();
    private Response lastResponse;
    private String username;

    @Etantdonnéque("un compte sans réservation existe")
    public void unCompteSansReservationExiste() {
        username = nextUsername();
        String body = """
                {
                  "username":"%s",
                  "homePlanetName":"Mars"
                }
                """.formatted(username);

        Response response = given()
                .contentType(JSON)
                .body(body)
                .post(ACCOUNTS_PATH);

        assertEquals(STATUS_CREATED, response.getStatusCode());
    }

    @Etantdonnéque("un compte avec la planète Earth existe")
    public void unCompteAvecLaPlaneteEarthExiste() {
        username = nextUsername();
        String body = """
                {
                  "username":"%s",
                  "homePlanetName":"Earth"
                }
                """.formatted(username);

        Response response = given()
                .contentType(JSON)
                .body(body)
                .post(ACCOUNTS_PATH);

        assertEquals(STATUS_CREATED, response.getStatusCode());
    }

    @Etantdonnéque("aucun compte n'existe pour ce nom d'utilisateur")
    public void aucunCompteNExistePourCeNom() {
        username = "unknown-account";
    }

    @Etantdonnéque("un compte avec des réservations existe")
    public void unCompteAvecDesReservationsExiste() {
        username = nextUsername();

        String accountBody = """
                {
                  "username":"%s",
                  "homePlanetName":"Mars"
                }
                """.formatted(username);

        Response accResp = given()
                .contentType(JSON)
                .body(accountBody)
                .post(ACCOUNTS_PATH);

        assertEquals(STATUS_CREATED, accResp.getStatusCode());

        String bookingBody = """
                {
                  "accountUsername":"%s",
                  "bookingDateTime":"%s",
                  "cabinType":"%s",
                  "travelers":[
                    {"name":"Traveler","category":"%s"}
                  ]
                }
                """.formatted(
                username,
                BOOKING_DATE_TIME,
                CABIN_TYPE_STANDARD,
                CATEGORY_ADULT
        );

        Response bookingResp = given()
                .contentType(JSON)
                .body(bookingBody)
                .post(CRUISES_PATH + DEFAULT_CRUISE + BOOKINGS_PATH);

        assertEquals(STATUS_CREATED, bookingResp.getStatusCode());
    }

    @Quand("je supprime ce compte")
    public void jeSupprimeCeCompte() {
        lastResponse = given()
                .delete(ACCOUNTS_PATH + "/" + username);
    }

    @Alors("la suppression retourne le statut {int}")
    public void laSuppressionRetourneLeStatut(int expectedStatus) {
        assertEquals(expectedStatus, lastResponse.getStatusCode());
    }

    @Alors("la suppression retourne le statut {int} et l'erreur {string} avec la description {string}")
    public void laSuppressionRetourneLeStatutEtErreurAvecDescription(
            int expectedStatus,
            String expectedError,
            String expectedDescription
    ) throws Exception {
        assertEquals(expectedStatus, lastResponse.getStatusCode());

        JsonNode root = mapper.readTree(lastResponse.getBody().asString());
        assertEquals(expectedError, root.get("error").asText());
        assertEquals(expectedDescription, root.get("description").asText());
    }

    @Alors("une requête pour consulter ce compte retourne le statut {int} et l'erreur {string}")
    public void uneRequetePourConsulterCeCompteRetourneStatutEtErreur(int expectedStatus, String expectedError) throws Exception {
        Response response = given()
                .get(ACCOUNTS_PATH + "/" + username);

        assertEquals(expectedStatus, response.getStatusCode());

        JsonNode root = mapper.readTree(response.getBody().asString());
        assertEquals(expectedError, root.get("error").asText());
    }

    private static String nextUsername() {
        String name = "bdd-delete-user-" + sequence;
        sequence++;
        return name;
    }
}
