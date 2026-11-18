package uspace.api.exceptions.mappers;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import uspace.api.exceptions.ErrorResponse;
import uspace.domain.exceptions.InvalidParameterException;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
class InvalidParameterExceptionMapperTest {

    private static final String ERROR_CODE = "INVALID_PARAMETER";
    private static final String DURATION_MESSAGE = "Excursion duration must be greater than 0.";
    private static final String CAPACITY_MESSAGE = "Excursion capacity must be greater than 0.";
    private static final int BAD_REQUEST_STATUS = 400;

    private final InvalidParameterExceptionMapper mapper = new InvalidParameterExceptionMapper();


    @Test
    void givenInvalidDuration_whenToResponse_thenReturn400AndExactMessage() {
        InvalidParameterException exception = new InvalidParameterException(DURATION_MESSAGE);

        var response = mapper.toResponse(exception);
        ErrorResponse body = (ErrorResponse) response.getEntity();

        assertEquals(BAD_REQUEST_STATUS, response.getStatus());
        assertEquals(ERROR_CODE, body.error());
        assertEquals(DURATION_MESSAGE, body.description());
    }

    @Test
    void givenInvalidCapacity_whenToResponse_thenReturn400AndExactMessage() {
        InvalidParameterException exception = new InvalidParameterException(CAPACITY_MESSAGE);

        var response = mapper.toResponse(exception);
        ErrorResponse body = (ErrorResponse) response.getEntity();

        assertEquals(BAD_REQUEST_STATUS, response.getStatus());
        assertEquals(ERROR_CODE, body.error());
        assertEquals(CAPACITY_MESSAGE, body.description());
    }
}
