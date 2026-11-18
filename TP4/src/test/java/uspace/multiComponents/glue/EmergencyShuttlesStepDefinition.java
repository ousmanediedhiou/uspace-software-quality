package uspace.multiComponents.glue;

import io.cucumber.java.fr.Quand;
import io.restassured.response.Response;
import uspace.multiComponents.util.APIHelper;
import uspace.multiComponents.util.ResponseMemory;

public class EmergencyShuttlesStepDefinition {

    @Quand("on demande le manifeste des navettes d'urgences pour la croisière {string}")
    public void onDemandeLattributionDesCabinesAvecLeCritere(String cruiseId) {
        Response response = APIHelper.getEmergencyShuttlesManifest(cruiseId);
        ResponseMemory.setResponse(response);
    }
}
