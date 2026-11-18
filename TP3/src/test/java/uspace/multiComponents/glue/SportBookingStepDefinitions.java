package uspace.multiComponents.glue;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.fr.Alors;
import io.cucumber.java.fr.Et;
import io.cucumber.java.fr.Etantdonnéque;
import io.cucumber.java.fr.Quand;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import uspace.UspaceMain;
import uspace.api.ConfigurationServerRest;
import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class SportBookingStepDefinitions {

    private static final String JSON = "application/json";
    private static final String DEFAULT_CRUISE = UspaceMain.DEFAULT_CRUISE_ID;

    private static final String BASE_URI = "http://localhost";
    private static final String ACCOUNTS_PATH = "/accounts";
    private static final String CRUISES_PATH = "/cruises/";
    private static final String BOOKINGS_PATH = "/bookings";
    private static final String TRAVELERS_PATH = "/travelers";
    private static final String SPORTS_PATH = "/sports";

    private static final int STATUS_CREATED = 201;
    private static final int STATUS_OK = 200;

    private static final String CATEGORY_ADULT = "ADULT";
    private static final String CATEGORY_SENIOR = "SENIOR";
    private static final String CATEGORY_CHILD = "CHILD";

    private static final String HOME_PLANET_EARTH = "Earth";
    private static final String CABIN_TYPE_STANDARD = "STANDARD";
    private static final String BOOKING_DATE_TIME = "2085-01-01T10:00";
    private static final String EMPTY_JSON_OBJECT = "{}";

    private static int sequence = 1;

    private final ObjectMapper mapper = new ObjectMapper();
    private Response lastResponse;

    private String bookingId;
    private String adultTravelerId;
    private String seniorTravelerId;
    private String childTravelerId;

    @Before
    public void init() {
        ConfigurationServerRest.useInMemoryRepositories();
        UspaceMain.runForTest();
        RestAssured.baseURI = BASE_URI;
        RestAssured.port = UspaceMain.PORT;
        createAccountAndBooking();
    }

    @After
    public void stop() {
        try {
            UspaceMain.stop();
        } catch (Exception ignored) {
        }
    }

    @Etantdonnéque("le serveur USpace est démarré")
    public void server_ok() {
    }

    @Etantdonnéque("une réservation avec un adulte, un senior et un enfant existe pour la croisière par défaut")
    public void booking_ok() {
    }

    @Quand("je réserve le sport {string} pour l'adulte")
    public void reserve_adult(String sport) {
        callSport(adultTravelerId, mapSportForAdult(sport));
    }

    @Quand("je réserve le sport {string} pour le senior")
    public void reserve_senior(String sport) {
        callSport(seniorTravelerId, mapSportForSenior(sport));
    }

    @Quand("je réserve le sport {string} pour l'enfant")
    public void reserve_child(String sport) {
        callSport(childTravelerId, mapSportForChild(sport));
    }

    @Quand("je réserve une première fois le sport {string} pour l'adulte")
    public void reserve_adult_first(String sport) {
        callSport(adultTravelerId, mapSportForAdult(sport));
    }

    @Quand("je réserve une deuxième fois le sport {string} pour l'adulte")
    public void reserve_adult_second(String sport) {
        callSport(adultTravelerId, mapSportForAdult(sport));
    }

    @Quand("j'envoie une requête POST pour réserver un sport sans champ sport")
    public void post_sport_without_required_field() {
        lastResponse = callSportRaw(adultTravelerId, EMPTY_JSON_OBJECT);
    }

    @Quand("j'envoie une requête POST pour réserver le sport {string} pour une croisière inexistante")
    public void post_sport_for_unknown_cruise(String featureSport) {
        String sport = mapSportForAdult(featureSport);
        lastResponse = given()
                .contentType(JSON)
                .body(buildSportRequestBody(sport))
                .post(
                        CRUISES_PATH + "UNKNOWN_CRUISE"
                                + BOOKINGS_PATH + "/" + bookingId
                                + TRAVELERS_PATH + "/" + adultTravelerId
                                + SPORTS_PATH
                );
    }

    @Alors("la réponse a le statut {int}")
    public void status(int expected) {
        assertEquals(expected, lastResponse.getStatusCode());
    }

    @Et("le corps JSON contient l'erreur {string} et la description {string}")
    public void json_error(String error, String desc) throws Exception {
        JsonNode root = mapper.readTree(lastResponse.getBody().asString());
        assertEquals(error, root.get("error").asText());
        assertEquals(desc, root.get("description").asText());
    }

    private Response callSportRaw(String travelerId, String body) {
        return given()
                .contentType(JSON)
                .body(body)
                .post(
                        CRUISES_PATH + DEFAULT_CRUISE
                                + BOOKINGS_PATH + "/" + bookingId
                                + TRAVELERS_PATH + "/" + travelerId
                                + SPORTS_PATH
                );
    }

    private void callSport(String travelerId, String sport) {
        lastResponse = callSportRaw(travelerId, buildSportRequestBody(sport));
    }

    private String buildSportRequestBody(String sport) {
        return """
                {
                  "sport":"%s"
                }
                """.formatted(sport);
    }

    private String mapSportForAdult(String featureName) {
        if ("Lunar Soccer".equals(featureName)) {
            return "Gravity-Free Tennis";
        }
        return featureName;
    }

    private String mapSportForSenior(String featureName) {
        if ("Lunar Soccer".equals(featureName)) {
            return "Moonlight Yoga";
        }
        if ("Galactic Tennis".equals(featureName)) {
            return "Gravity Soccer";
        }
        return featureName;
    }

    private String mapSportForChild(String featureName) {
        if ("Zero-G Boxing".equals(featureName)) {
            return "Cosmic Martial Arts";
        }
        return featureName;
    }

    private static String nextUsername() {
        String name = "bdd-user-" + sequence;
        sequence++;
        return name;
    }

    private void createAccountAndBooking() {
        String username = nextUsername();

        String acc = """
                {
                  "username":"%s",
                  "homePlanetName":"%s"
                }
                """.formatted(username, HOME_PLANET_EARTH);

        Response accResp = given()
                .contentType(JSON)
                .body(acc)
                .post(ACCOUNTS_PATH);

        assertEquals(STATUS_CREATED, accResp.getStatusCode());

        String booking = """
                {
                  "accountUsername":"%s",
                  "bookingDateTime":"%s",
                  "cabinType":"%s",
                  "travelers":[
                    {"name":"Adult","category":"%s"},
                    {"name":"Senior","category":"%s"},
                    {"name":"Child","category":"%s"}
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
            JsonNode root = mapper.readTree(get.getBody().asString());
            JsonNode travelers = root.get("travelers");
            travelers.forEach(t -> {
                String cat = t.get("category").asText();
                String id = t.get("id").asText();

                if (CATEGORY_ADULT.equals(cat)) adultTravelerId = id;
                if (CATEGORY_SENIOR.equals(cat)) seniorTravelerId = id;
                if (CATEGORY_CHILD.equals(cat)) childTravelerId = id;
            });
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
