package uspace.api.exceptions.mappers;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import uspace.api.exceptions.ErrorResponse;
import uspace.domain.cruise.itinerary.planet.excursion.exceptions.InvalidExcursionException;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
class InvalidExcursionExceptionMapperTest {

    private static final String ERROR_CODE = "INVALID_EXCURSION";
    private static final String EXPECTED_MESSAGE = "Invalid excursion.";
    private static final int BAD_REQUEST_STATUS = 400;
    private static final String ANY_EXCEPTION_MESSAGE = "whatever";

    private final InvalidExcursionExceptionMapper mapper = new InvalidExcursionExceptionMapper();


    @Test
    void givenInvalidExcursion_whenToResponse_thenReturn400AndExactMessage() {
        InvalidExcursionException exception = new InvalidExcursionException(ANY_EXCEPTION_MESSAGE);

        var response = mapper.toResponse(exception);
        ErrorResponse body = (ErrorResponse) response.getEntity();

        assertEquals(BAD_REQUEST_STATUS, response.getStatus());
        assertEquals(ERROR_CODE, body.error());
        assertEquals(EXPECTED_MESSAGE, body.description());
    }
}
