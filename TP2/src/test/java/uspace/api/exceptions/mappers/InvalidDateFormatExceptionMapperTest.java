package uspace.api.exceptions.mappers;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import uspace.api.exceptions.ErrorResponse;
import uspace.domain.exceptions.InvalidDateFormatException;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
class InvalidDateFormatExceptionMapperTest {

    private static final String ERROR_CODE = "INVALID_DATE_FORMAT";
    private static final String EXPECTED_MESSAGE = "Invalid date format.";
    private static final int BAD_REQUEST_STATUS = 400;

    private final InvalidDateFormatExceptionMapper mapper = new InvalidDateFormatExceptionMapper();

    @Test
    void givenInvalidDateFormat_whenToResponse_thenReturn400AndExactMessage() {
        InvalidDateFormatException exception = new InvalidDateFormatException();

        var response = mapper.toResponse(exception);
        ErrorResponse body = (ErrorResponse) response.getEntity();

        assertEquals(BAD_REQUEST_STATUS, response.getStatus());
        assertEquals(ERROR_CODE, body.error());
        assertEquals(EXPECTED_MESSAGE, body.description());
    }
}
