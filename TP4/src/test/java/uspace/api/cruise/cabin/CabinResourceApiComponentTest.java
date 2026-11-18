package uspace.api.cruise.cabin;

import jakarta.ws.rs.client.Invocation;
import jakarta.ws.rs.core.Application;
import jakarta.ws.rs.core.Response;
import org.glassfish.jersey.jackson.JacksonFeature;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.test.JerseyTest;
import org.junit.jupiter.api.Test;
import uspace.api.exceptions.ErrorResponse;
import uspace.api.exceptions.mappers.uspace.CruiseNotFoundExceptionMapper;
import uspace.api.exceptions.mappers.uspace.InvalidCabinAttributionCriteriaExceptionMapper;
import uspace.application.cruise.cabin.CabinAttributionService;
import uspace.application.cruise.cabin.dtos.CabinAttributionDto;
import uspace.application.cruise.cabin.dtos.CabinAttributionsDto;
import uspace.domain.cruise.cabin.exceptions.InvalidCabinAttributionCriteriaException;
import uspace.domain.cruise.exceptions.CruiseNotFoundException;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class CabinResourceApiComponentTest extends JerseyTest {

    private static final String ANY_CRUISE_ID = "ANY_CRUISE_ID";
    private static final String VALID_CRITERIA = "bookingDateTime";
    private static final String INVALID_CRITERIA = "invalidCriteria";
    private static final String PATH_CRUISES = "/cruises";
    private static final String PATH_CABINS = "/cabins";

    private static final String ANY_CABIN_ID = "A1";
    private static final String ANY_BOOKING_ID = "BOOKING_ID";
    private static final String ANY_CATEGORY = "STANDARD";

    private static final String CRUISE_NOT_FOUND_ERROR_CODE = "CRUISE_NOT_FOUND";
    private static final String CRUISE_NOT_FOUND_DESCRIPTION = "Cruise not found.";
    private static final String INVALID_CRITERIA_ERROR_CODE = "INVALID_CABIN_ATTRIBUTION_CRITERIA";

    private CabinAttributionService cabinAttributionServiceMock;

    @Override
    protected Application configure() {
        cabinAttributionServiceMock = mock(CabinAttributionService.class);

        return new ResourceConfig()
                .register(JacksonFeature.withoutExceptionMappers())
                .register(CruiseNotFoundExceptionMapper.class)
                .register(InvalidCabinAttributionCriteriaExceptionMapper.class)
                .register(new CabinResource(cabinAttributionServiceMock));
    }

    @Test
    void givenExistingCruiseAndValidCriteria_whenGetCabins_thenReturnCabinsInBody() {
        CabinAttributionsDto expectedDto = cabinAttributionsDto();

        when(cabinAttributionServiceMock.getCabinAttributions(ANY_CRUISE_ID, VALID_CRITERIA))
                .thenReturn(expectedDto);

        Response response = whenGetCabinsWithCriteria(VALID_CRITERIA);

        CabinAttributionsDto actualDto = response.readEntity(CabinAttributionsDto.class);

        List<String> actualCabinData = actualDto.cabins.stream()
                .map(cabin -> cabin.cabinId + "|" + cabin.bookingId + "|" + cabin.category)
                .toList();
        List<String> expectedCabinData = expectedDto.cabins.stream()
                .map(cabin -> cabin.cabinId + "|" + cabin.bookingId + "|" + cabin.category)
                .toList();

        assertEquals(expectedCabinData, actualCabinData);
    }

    @Test
    void givenNonExistingCruise_whenGetCabins_thenReturnHttp404WithCruiseNotFoundError() {
        doThrow(new CruiseNotFoundException())
                .when(cabinAttributionServiceMock)
                .getCabinAttributions(ANY_CRUISE_ID, VALID_CRITERIA);

        Response response = whenGetCabinsWithCriteria(VALID_CRITERIA);

        ErrorResponse errorResponse = response.readEntity(ErrorResponse.class);
        String actual =
                response.getStatus() + "|" + errorResponse.error + "|" + errorResponse.description;
        String expected =
                Response.Status.NOT_FOUND.getStatusCode() + "|" + CRUISE_NOT_FOUND_ERROR_CODE + "|" + CRUISE_NOT_FOUND_DESCRIPTION;

        assertEquals(expected, actual);
    }

    @Test
    void givenInvalidCriteria_whenGetCabins_thenReturnHttp400WithInvalidCriteriaError() {
        doThrow(new InvalidCabinAttributionCriteriaException())
                .when(cabinAttributionServiceMock)
                .getCabinAttributions(ANY_CRUISE_ID, INVALID_CRITERIA);

        Response response = whenGetCabinsWithCriteria(INVALID_CRITERIA);

        ErrorResponse errorResponse = response.readEntity(ErrorResponse.class);
        String actual =
                response.getStatus() + "|" + errorResponse.error;
        String expected =
                Response.Status.BAD_REQUEST.getStatusCode() + "|" + INVALID_CRITERIA_ERROR_CODE;

        assertEquals(expected, actual);
    }

    private Response whenGetCabinsWithCriteria(String criteria) {
        Invocation.Builder request = target(PATH_CRUISES + "/" + ANY_CRUISE_ID + PATH_CABINS)
                .queryParam("criteria", criteria)
                .request();

        return request.get();
    }

    private CabinAttributionsDto cabinAttributionsDto() {
        CabinAttributionDto cabinAttributionDto =
                new CabinAttributionDto(ANY_CABIN_ID, ANY_BOOKING_ID, ANY_CATEGORY);

        return new CabinAttributionsDto(List.of(cabinAttributionDto));
    }
}
