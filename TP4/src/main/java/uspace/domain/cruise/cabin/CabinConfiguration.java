package uspace.domain.cruise.cabin;

import java.util.List;
import java.util.Map;

public interface CabinConfiguration {
    Map<CabinType, List<String>> findCabinIdsByType();
}
