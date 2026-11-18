package uspace.multiComponents.glue;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.cucumber.java.fr.Alors;
import io.cucumber.java.fr.Etantdonnéque;
import io.cucumber.java.fr.Quand;
import io.restassured.response.Response;

import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class CreateAccountStepDefinitions {

    private static final String JSON = "application/json";
    private static final String ACCOUNTS_PATH = "/accounts";
    private static final String DEFAULT_HOME_PLANET = "Mars";
    private static final String FIELD_ERROR = "error";
    private static final String FIELD_DESCRIPTION = "description";
    private static final String ERROR_MISSING_PARAMETER = "MISSING_PARAMETER";
    private static final String ERROR_USERNAME_ALREADY_EXISTS = "USERNAME_ALREADY_EXISTS";

    private static int sequence = 1;

    private final ObjectMapper mapper = new ObjectMapper();
    private Response lastResponse;
    private String username;
    private String homePlanetName;

    @Etantdonnéque("un compte utilisateur valide à créer")
    public void unCompteUtilisateurValideACreer() {
        username = nextUsername();
        homePlanetName = DEFAULT_HOME_PLANET;
    }

    @Etantdonnéque("un compte utilisateur sans username")
    public void unCompteUtilisateurSansUsername() {
        username = "";
        homePlanetName = DEFAULT_HOME_PLANET;
    }

    @Etantdonnéque("un compte utilisateur sans homePlanetName")
    public void unCompteUtilisateurSansHomePlanetName() {
        username = nextUsername();
        homePlanetName = "";
    }

    @Etantdonnéque("un compte utilisateur avec un username déjà utilisé")
    public void unCompteUtilisateurAvecUnUsernameDejaUtilise() {
        username = nextUsername();
        homePlanetName = DEFAULT_HOME_PLANET;

        String body = """
                {
                  "username":"%s",
                  "homePlanetName":"%s"
                }
                """.formatted(username, homePlanetName);

        Response response = given()
                .contentType(JSON)
                .body(body)
                .post(ACCOUNTS_PATH);

        assertEquals(201, response.getStatusCode());
    }

    @Quand("j'envoie la requête de création du compte")
    public void jEnvoieLaRequeteDeCreationDuCompte() {
        String body = """
                {
                  "username":"%s",
                  "homePlanetName":"%s"
                }
                """.formatted(username, homePlanetName);

        lastResponse = given()
                .contentType(JSON)
                .body(body)
                .post(ACCOUNTS_PATH);
    }

    @Alors("la création retourne le statut {int}")
    public void laCreationRetourneLeStatut(int expectedStatus) {
        assertEquals(expectedStatus, lastResponse.getStatusCode());
    }

    @Alors("la création retourne le statut {int} et l'erreur {string} pour le paramètre {string}")
    public void laCreationRetourneLeStatutEtErreurPourLeParametre(
            int expectedStatus,
            String expectedError,
            String parameterName
    ) throws Exception {
        assertEquals(expectedStatus, lastResponse.getStatusCode());

        JsonNode root = mapper.readTree(lastResponse.getBody().asString());
        assertEquals(ERROR_MISSING_PARAMETER, root.get(FIELD_ERROR).asText());
        String expectedDescription = "Missing parameter: " + parameterName;
        assertEquals(expectedDescription, root.get(FIELD_DESCRIPTION).asText());
        assertEquals(expectedError, ERROR_MISSING_PARAMETER);
    }

    @Alors("la création retourne le statut {int} et l'erreur {string} avec la description {string}")
    public void laCreationRetourneLeStatutEtErreurAvecDescription(
            int expectedStatus,
            String expectedError,
            String expectedDescription
    ) throws Exception {
        assertEquals(expectedStatus, lastResponse.getStatusCode());

        JsonNode root = mapper.readTree(lastResponse.getBody().asString());
        assertEquals(ERROR_USERNAME_ALREADY_EXISTS, root.get(FIELD_ERROR).asText());
        assertEquals(expectedDescription, root.get(FIELD_DESCRIPTION).asText());
        assertEquals(expectedError, ERROR_USERNAME_ALREADY_EXISTS);
    }

    @Alors("le compte créé peut être consulté avec succès")
    public void leCompteCreePeutEtreConsulteAvecSucces() throws Exception {
        Response response = given()
                .get(ACCOUNTS_PATH + "/" + username);

        assertEquals(200, response.getStatusCode());

        JsonNode root = mapper.readTree(response.getBody().asString());
        assertEquals(username, root.get("username").asText());
    }

    private static String nextUsername() {
        String name = "bdd-create-user-" + sequence;
        sequence++;
        return name;
    }
}
