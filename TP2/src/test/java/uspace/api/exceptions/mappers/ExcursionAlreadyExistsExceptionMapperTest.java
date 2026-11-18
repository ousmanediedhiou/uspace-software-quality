package uspace.api.exceptions.mappers;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import uspace.api.exceptions.ErrorResponse;
import uspace.domain.cruise.itinerary.planet.excursion.exceptions.ExcursionAlreadyExistsException;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
class ExcursionAlreadyExistsExceptionMapperTest {

    private static final String ERROR_CODE = "EXCURSION_ALREADY_EXISTS";
    private static final String EXPECTED_MESSAGE = "Excursion already exists in the cruise itinerary.";
    private static final int BAD_REQUEST_STATUS = 400;

    private final ExcursionAlreadyExistsExceptionMapper mapper = new ExcursionAlreadyExistsExceptionMapper();

    @Test
    void givenNameDuplicated_whenToResponse_thenReturn400AndExactMessage() {
        ExcursionAlreadyExistsException exception = new ExcursionAlreadyExistsException();

        var response = mapper.toResponse(exception);
        ErrorResponse body = (ErrorResponse) response.getEntity();

        assertEquals(BAD_REQUEST_STATUS, response.getStatus());
        assertEquals(ERROR_CODE, body.error());
        assertEquals(EXPECTED_MESSAGE, body.description());
    }
}
