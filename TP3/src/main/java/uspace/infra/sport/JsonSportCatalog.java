package uspace.infra.sport;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import uspace.application.cruise.sport.SportCatalog;
import uspace.domain.cruise.CruiseId;
import uspace.domain.cruise.sport.Sport;
import uspace.domain.cruise.sport.SportAudience;

import java.io.InputStream;

public class JsonSportCatalog implements SportCatalog {

    private static final String SPORTS_FILE = "/data/sports.json";
    private final ObjectMapper mapper = new ObjectMapper();

    @Override
    public Sport findSportForCruise(CruiseId cruiseId, String sportName) {
        try {
            InputStream inputStream = getClass().getResourceAsStream(SPORTS_FILE);
            if (inputStream == null) {
                return null;
            }

            JsonNode root = mapper.readTree(inputStream);
            JsonNode sports = root.get("sports");
            if (sports == null || !sports.isArray()) {
                return null;
            }

            for (JsonNode sportNode : sports) {
                String name = sportNode.get("name").asText();

                if (!name.equalsIgnoreCase(sportName)) {
                    continue;
                }

                JsonNode cruises = sportNode.get("cruises");
                if (cruises == null || !cruises.isArray()) {
                    continue;
                }

                boolean cruiseMatches = false;
                for (JsonNode cruiseNode : cruises) {
                    CruiseId sportCruiseId = new CruiseId(cruiseNode.asText());
                    if (cruiseId.equals(sportCruiseId)) {
                        cruiseMatches = true;
                        break;
                    }
                }

                if (!cruiseMatches) {
                    continue;
                }

                String audience = sportNode.get("audience").asText();
                SportAudience sportAudience = SportAudience.valueOf(audience);

                return new Sport(name, sportAudience);
            }

            return null;

        } catch (Exception e) {
            return null;
        }
    }
}
