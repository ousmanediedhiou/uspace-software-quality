package uspace.api.exceptions.mappers;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import uspace.api.exceptions.ErrorResponse;
import uspace.domain.cruise.exceptions.CruiseNotFoundException;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
class CruiseNotFoundExceptionMapperTest {

    private static final String ERROR_CODE = "CRUISE_NOT_FOUND";
    private static final String EXPECTED_MESSAGE = "Cruise not found.";
    private static final int NOT_FOUND_STATUS = 404;

    private final CruiseNotFoundExceptionMapper mapper = new CruiseNotFoundExceptionMapper();


    @Test
    void givenCruiseNotFound_whenToResponse_thenReturn404AndExactMessage() {
        CruiseNotFoundException exception = new CruiseNotFoundException();

        var response = mapper.toResponse(exception);
        ErrorResponse body = (ErrorResponse) response.getEntity();

        assertEquals(NOT_FOUND_STATUS, response.getStatus());
        assertEquals(ERROR_CODE, body.error());
        assertEquals(EXPECTED_MESSAGE, body.description());
    }
}
