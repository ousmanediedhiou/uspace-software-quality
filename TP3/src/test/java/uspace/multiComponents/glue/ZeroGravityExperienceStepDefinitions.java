package uspace.multiComponents.glue;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.cucumber.java.fr.*;
import io.restassured.response.Response;
import uspace.UspaceMain;

import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class ZeroGravityExperienceStepDefinitions {

    private static final String JSON = "application/json";
    private static final String DEFAULT_CRUISE = UspaceMain.DEFAULT_CRUISE_ID;

    private static final String CRUISES_PATH = "/cruises/";
    private static final String BOOKINGS_PATH = "/bookings";
    private static final String TRAVELERS_PATH = "/travelers";
    private static final String ZERO_GRAVITY_PATH = "/zeroGravityExperiences";

    private static final int STATUS_CREATED = 201;
    private static final int STATUS_OK = 200;

    private static final String CATEGORY_ADULT = "ADULT";
    private static final String CATEGORY_SENIOR = "SENIOR";
    private static final String CATEGORY_CHILD = "CHILD";

    private static final String HOME_PLANET_EARTH = "Earth";
    private static final String CABIN_TYPE_STANDARD = "STANDARD";
    private static final String BOOKING_DATE_TIME = "2085-01-01T10:00";
    private static final String LATE_BOOKING_DATE_TIME = "2100-01-01T10:00";
    private static final String EMPTY_JSON_OBJECT = "{}";

    private static final String UNKNOWN_BOOKING_ID = "00000000-0000-0000-0000-000000000001";
    private static final String UNKNOWN_TRAVELER_ID = "00000000-0000-0000-0000-000000000002";

    private static int globalUsernameSequence = 1;

    private final ObjectMapper mapper = new ObjectMapper();

    private Response lastResponse;

    private String bookingId;
    private String adultTravelerId;
    private String seniorTravelerId;
    private String childTravelerId;

    @Etantdonnéque("une réservation avec un adulte, un senior et un enfant existe pour la croisière par défaut pour les tests zéro gravité")
    public void createBookingWithAdultSeniorChild() {
        createAccountAndBooking();
    }

    @Étantdonnéque("la capacité maximale de l'expérience zéro gravité est atteinte pour la croisière par défaut")
    public void la_capacite_maximale_est_atteinte() {
        fillZeroGravityCapacity();
        createAccountAndBooking();
    }

    @Etantdonnéque("aucune réservation n'existe pour la croisière par défaut pour les tests zéro gravité")
    public void noBookingExistsForDefaultCruise() {
        bookingId = UNKNOWN_BOOKING_ID;
    }

    @Quand("je réserve une expérience zéro gravité pour l'adulte à la date {string}")
    public void reserveZeroGForAdult(String dateTime) {
        callZeroGravityExperience(adultTravelerId, buildZeroGRequestBody(dateTime));
    }

    @Quand("je réserve une expérience zéro gravité pour le senior à la date {string}")
    public void reserveZeroGForSenior(String dateTime) {
        callZeroGravityExperience(seniorTravelerId, buildZeroGRequestBody(dateTime));
    }

    @Quand("je réserve une expérience zéro gravité pour l'enfant à la date {string}")
    public void reserveZeroGForChild(String dateTime) {
        callZeroGravityExperience(childTravelerId, buildZeroGRequestBody(dateTime));
    }

    @Quand("je réserve une expérience zéro gravité pour l'adulte à une date après le départ de la croisière")
    public void reserveZeroGForAdultAfterDeparture() {
        callZeroGravityExperience(adultTravelerId, buildZeroGRequestBody(LATE_BOOKING_DATE_TIME));
    }

    @Quand("je réserve à nouveau une expérience zéro gravité pour le même adulte à la date {string}")
    public void reserveZeroGAgainForSameAdult(String dateTime) {
        callZeroGravityExperience(adultTravelerId, buildZeroGRequestBody(dateTime));
    }

    @Quand("je réserve une expérience zéro gravité pour un voyageur dans une réservation inexistante")
    public void reserveZeroGInUnknownBooking() {
        String travelerId = UNKNOWN_TRAVELER_ID;
        callZeroGravityExperience(travelerId, buildZeroGRequestBody(BOOKING_DATE_TIME));
    }

    @Quand("je réserve une expérience zéro gravité pour un voyageur inexistant de cette réservation")
    public void reserveZeroGForUnknownTraveler() {
        String travelerId = UNKNOWN_TRAVELER_ID;
        callZeroGravityExperience(travelerId, buildZeroGRequestBody(BOOKING_DATE_TIME));
    }

    @Quand("j'envoie une requête POST zéro gravité sans champ obligatoire pour l'adulte")
    public void postZeroGWithoutRequiredFieldForAdult() {
        callZeroGravityExperience(adultTravelerId, EMPTY_JSON_OBJECT);
    }

    @Alors("la réponse HTTP pour zéro gravité est {int}")
    public void zeroGravityStatus(int status) {
        assertEquals(status, lastResponse.getStatusCode());
    }

    @Et("le code d'erreur zéro gravité est {string} et la description {string}")
    public void zeroGravityError(String error, String desc) throws Exception {
        JsonNode root = mapper.readTree(lastResponse.getBody().asString());
        assertEquals(error, root.get("error").asText());
        assertEquals(desc, root.get("description").asText());
    }

    @Et("le code d'erreur zéro gravité est {string}")
    public void zeroGravityErrorCode(String error) throws Exception {
        JsonNode root = mapper.readTree(lastResponse.getBody().asString());
        assertEquals(error, root.get("error").asText());
    }


    private void callZeroGravityExperience(String travelerId, String body) {
        lastResponse = given()
                .contentType(JSON)
                .body(body)
                .post(
                        CRUISES_PATH + DEFAULT_CRUISE
                                + BOOKINGS_PATH + "/" + bookingId
                                + TRAVELERS_PATH + "/" + travelerId
                                + ZERO_GRAVITY_PATH
                );
    }

    private String buildZeroGRequestBody(String dateTime) {
        return """
                {
                  "experienceBookingDateTime":"%s"
                }
                """.formatted(dateTime);
    }

    private static String generateUsername(String prefix) {
        String username = prefix + globalUsernameSequence;
        globalUsernameSequence++;
        return username;
    }

    private void createAccountAndBooking() {
        String username = generateUsername("bdd-user-zeroG-");

        String acc = """
                {
                  "username":"%s",
                  "homePlanetName":"%s"
                }
                """.formatted(username, HOME_PLANET_EARTH);

        Response accResp = given()
                .contentType(JSON)
                .body(acc)
                .post("/accounts");

        assertEquals(STATUS_CREATED, accResp.getStatusCode());

        String booking = """
                {
                  "accountUsername":"%s",
                  "bookingDateTime":"%s",
                  "cabinType":"%s",
                  "travelers":[
                    {"name":"Adult Traveller","category":"%s"},
                    {"name":"Senior Traveller","category":"%s"},
                    {"name":"Child Traveller","category":"%s"}
                  ]
                }
                """.formatted(
                username,
                BOOKING_DATE_TIME,
                CABIN_TYPE_STANDARD,
                CATEGORY_ADULT,
                CATEGORY_SENIOR,
                CATEGORY_CHILD
        );

        Response bResp = given()
                .contentType(JSON)
                .body(booking)
                .post(CRUISES_PATH + DEFAULT_CRUISE + BOOKINGS_PATH);

        assertEquals(STATUS_CREATED, bResp.getStatusCode());

        String loc = bResp.getHeader("Location");
        bookingId = loc.substring(loc.lastIndexOf('/') + 1);

        Response get = given()
                .get(CRUISES_PATH + DEFAULT_CRUISE + BOOKINGS_PATH + "/" + bookingId);

        assertEquals(STATUS_OK, get.getStatusCode());

        try {
            JsonNode json = mapper.readTree(get.getBody().asString());
            JsonNode travelers = json.get("travelers");
            travelers.forEach(t -> {
                String cat = t.get("category").asText();
                String id = t.get("id").asText();
                switch (cat) {
                    case CATEGORY_ADULT -> adultTravelerId = id;
                    case CATEGORY_SENIOR -> seniorTravelerId = id;
                    case CATEGORY_CHILD -> childTravelerId = id;
                    default -> {
                    }
                }
            });
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private void fillZeroGravityCapacity() {
        for (int i = 0; i < 5; i++) {
            createBookingAndReserveZeroGForTwoAdults();
        }
    }

    private void createBookingAndReserveZeroGForTwoAdults() {
        String username = generateUsername("bdd-user-zeroG-full-");

        String acc = """
                {
                  "username":"%s",
                  "homePlanetName":"%s"
                }
                """.formatted(username, HOME_PLANET_EARTH);

        Response accResp = given()
                .contentType(JSON)
                .body(acc)
                .post("/accounts");

        assertEquals(STATUS_CREATED, accResp.getStatusCode());

        String booking = """
                {
                  "accountUsername":"%s",
                  "bookingDateTime":"%s",
                  "cabinType":"%s",
                  "travelers":[
                    {"name":"Adult Traveller 1","category":"%s"},
                    {"name":"Adult Traveller 2","category":"%s"}
                  ]
                }
                """.formatted(
                username,
                BOOKING_DATE_TIME,
                CABIN_TYPE_STANDARD,
                CATEGORY_ADULT,
                CATEGORY_ADULT
        );

        Response bResp = given()
                .contentType(JSON)
                .body(booking)
                .post(CRUISES_PATH + DEFAULT_CRUISE + BOOKINGS_PATH);

        assertEquals(STATUS_CREATED, bResp.getStatusCode());

        String loc = bResp.getHeader("Location");
        String localBookingId = loc.substring(loc.lastIndexOf('/') + 1);

        Response get = given()
                .get(CRUISES_PATH + DEFAULT_CRUISE + BOOKINGS_PATH + "/" + localBookingId);

        assertEquals(STATUS_OK, get.getStatusCode());

        try {
            JsonNode json = mapper.readTree(get.getBody().asString());
            JsonNode travelers = json.get("travelers");
            for (JsonNode traveler : travelers) {
                String travelerId = traveler.get("id").asText();
                Response zeroGResp = given()
                        .contentType(JSON)
                        .body(buildZeroGRequestBody(BOOKING_DATE_TIME))
                        .post(
                                CRUISES_PATH + DEFAULT_CRUISE
                                        + BOOKINGS_PATH + "/" + localBookingId
                                        + TRAVELERS_PATH + "/" + travelerId
                                        + ZERO_GRAVITY_PATH
                        );
                assertEquals(STATUS_OK, zeroGResp.getStatusCode());
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
