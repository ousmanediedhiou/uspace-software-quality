package uspace.application.cruise.sport;

import uspace.domain.cruise.CruiseId;
import uspace.domain.cruise.sport.Sport;

public interface SportCatalog {
    Sport findSportForCruise(CruiseId cruiseId, String sportName);
}
