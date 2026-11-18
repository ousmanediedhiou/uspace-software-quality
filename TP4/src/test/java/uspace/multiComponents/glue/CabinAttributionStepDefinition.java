package uspace.multiComponents.glue;

import io.cucumber.java.fr.Quand;
import io.restassured.response.Response;
import uspace.multiComponents.util.APIHelper;
import uspace.multiComponents.util.ResponseMemory;

public class CabinAttributionStepDefinition {

    private static final String CRUISE_ID = "JUPITER_MOONS_EXPLORATION_2085";

    @Quand("on demande l'attribution des cabines avec le critère {string}")
    public void onDemandeLattributionDesCabinesAvecLeCritere(String criteria) {
        Response response = APIHelper.getCabinAttributions(CRUISE_ID, criteria);
        ResponseMemory.setResponse(response);
    }
}
