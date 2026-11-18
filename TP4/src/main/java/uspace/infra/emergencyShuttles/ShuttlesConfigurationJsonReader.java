package uspace.infra.emergencyShuttles;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import uspace.domain.cruise.emergencyShuttles.ShuttleType;
import uspace.domain.cruise.emergencyShuttles.ShuttlesConfiguration;
import uspace.infra.cabin.CabinJsonReader;
import uspace.infra.emergencyShuttles.dtos.ShuttleConfigurationDto;
import uspace.infra.emergencyShuttles.dtos.ShuttlesConfigurationFileDto;

import java.io.FileInputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

public class ShuttlesConfigurationJsonReader implements ShuttlesConfiguration {
    private static final Logger logger = LoggerFactory.getLogger(CabinJsonReader.class);

    private final String shuttlesConfigurationJsonPath;

    public ShuttlesConfigurationJsonReader(String shuttlesConfigurationJsonPath) {
        this.shuttlesConfigurationJsonPath = shuttlesConfigurationJsonPath;
    }

    @Override
    public ShuttleConfigurationDto getShuttleConfigurationByType(ShuttleType type){
        String shuttlesConfigurationFileJson = readShuttlesConfigurationJson();
        ShuttlesConfigurationFileDto shuttlesConfigurationFileDto = parseShuttlesConfigurationJson(shuttlesConfigurationFileJson);

        ShuttleConfigurationDto configuration = shuttlesConfigurationFileDto.shuttleTypes.stream()
                .filter(s -> ShuttleType.fromString(s.type) == type)
                .findFirst()
                .get();

        return configuration;
    }

    private String readShuttlesConfigurationJson(){
        try (FileInputStream inputStream = new FileInputStream(shuttlesConfigurationJsonPath)) {
            return new String(inputStream.readAllBytes(), StandardCharsets.UTF_8);
        } catch (IOException exception) {
            logger.error("Error while trying to read cabin json file " + shuttlesConfigurationJsonPath, exception);
            throw new RuntimeException(exception);
        }
    }

    private ShuttlesConfigurationFileDto parseShuttlesConfigurationJson(String shuttlesConfigurationJson){
        try {
            return new ObjectMapper().readValue(shuttlesConfigurationJson, ShuttlesConfigurationFileDto.class);
        } catch (JsonProcessingException exception) {
            logger.error("Error while trying to parse cabin json file " + shuttlesConfigurationJsonPath, exception);
            throw new RuntimeException(exception);
        }
    }
}
