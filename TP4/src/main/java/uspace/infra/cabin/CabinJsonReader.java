package uspace.infra.cabin;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import uspace.domain.cruise.cabin.CabinConfiguration;
import uspace.domain.cruise.cabin.CabinType;
import uspace.infra.cabin.dtos.CabinCategoryDto;
import uspace.infra.cabin.dtos.CabinFileDto;

import java.io.FileInputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;

public class CabinJsonReader implements CabinConfiguration {
    private static final Logger logger = LoggerFactory.getLogger(CabinJsonReader.class);

    private final String cabinsJsonPath;

    public CabinJsonReader(String cabinsJsonPath) {
        this.cabinsJsonPath = cabinsJsonPath;
    }

    @Override
    public Map<CabinType, List<String>> findCabinIdsByType() {
        String cabinsJson = readCabinsJson();
        CabinFileDto cabinFileDto = parseCabinsJson(cabinsJson);

        Map<CabinType, List<String>> cabinsByType = new EnumMap<>(CabinType.class);

        for (CabinCategoryDto cabinCategoryDto : cabinFileDto.cabins) {
            CabinType cabinType = CabinType.valueOf(cabinCategoryDto.category);
            cabinsByType.put(cabinType, List.copyOf(cabinCategoryDto.ids));
        }

        return cabinsByType;
    }

    private String readCabinsJson() {
        try (FileInputStream inputStream = new FileInputStream(cabinsJsonPath)) {
            return new String(inputStream.readAllBytes(), StandardCharsets.UTF_8);
        } catch (IOException exception) {
            logger.error("Error while trying to read cabin json file " + cabinsJsonPath, exception);
            throw new RuntimeException(exception);
        }
    }

    private CabinFileDto parseCabinsJson(String cabinsJson) {
        try {
            return new ObjectMapper().readValue(cabinsJson, CabinFileDto.class);
        } catch (JsonProcessingException exception) {
            logger.error("Error while trying to parse cabin json file " + cabinsJsonPath, exception);
            throw new RuntimeException(exception);
        }
    }
}
