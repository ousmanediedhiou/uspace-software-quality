package uspace.api.exceptions.mappers;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import uspace.api.exceptions.ErrorResponse;
import uspace.domain.cruise.itinerary.exceptions.InvalidPlanetException;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
class ItineraryInvalidPlanetExceptionMapperTest {

    private static final String ERROR_CODE = "PLANET_NOT_FOUND";
    private static final String EXPECTED_MESSAGE = "Planet not found.";
    private static final int NOT_FOUND_STATUS = 404;

    private final ItineraryInvalidPlanetExceptionMapper mapper = new ItineraryInvalidPlanetExceptionMapper();

    @Test
    void givenPlanetNotFound_whenToResponse_thenReturn404AndExactMessage() {
        InvalidPlanetException exception = new InvalidPlanetException();

        var response = mapper.toResponse(exception);
        ErrorResponse body = (ErrorResponse) response.getEntity();

        assertEquals(NOT_FOUND_STATUS, response.getStatus());
        assertEquals(ERROR_CODE, body.error());
        assertEquals(EXPECTED_MESSAGE, body.description());
    }
}
