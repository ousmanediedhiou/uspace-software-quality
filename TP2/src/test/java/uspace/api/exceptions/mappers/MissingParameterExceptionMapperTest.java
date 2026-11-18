package uspace.api.exceptions.mappers;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import uspace.api.exceptions.ErrorResponse;
import uspace.domain.exceptions.MissingParameterException;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
class MissingParameterExceptionMapperTest {

    private static final String ERROR_CODE = "MISSING_PARAMETER";
    private static final String EXPECTED_MESSAGE = "Missing parameter: startDateTime";
    private static final int BAD_REQUEST_STATUS = 400;

    private final MissingParameterExceptionMapper mapper = new MissingParameterExceptionMapper();

    @Test
    void givenMissingParameter_whenToResponse_thenReturn400AndExactMessage() {
        MissingParameterException exception = new MissingParameterException("startDateTime");

        var response = mapper.toResponse(exception);
        ErrorResponse body = (ErrorResponse) response.getEntity();

        assertEquals(BAD_REQUEST_STATUS, response.getStatus());
        assertEquals(ERROR_CODE, body.error());
        assertEquals(EXPECTED_MESSAGE, body.description());
    }
}
