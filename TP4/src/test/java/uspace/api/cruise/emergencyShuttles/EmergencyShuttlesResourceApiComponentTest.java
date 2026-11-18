package uspace.api.cruise.emergencyShuttles;

import jakarta.ws.rs.client.Invocation;
import jakarta.ws.rs.core.Application;
import jakarta.ws.rs.core.Response;
import org.glassfish.jersey.jackson.JacksonFeature;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.test.JerseyTest;
import org.junit.jupiter.api.Test;
import uspace.api.exceptions.ErrorResponse;
import uspace.api.exceptions.mappers.uspace.CruiseNotFoundExceptionMapper;
import uspace.application.cruise.emergencyShuttles.EmergencyShuttlesService;
import uspace.application.cruise.emergencyShuttles.dtos.EmergencyShuttlesDto;
import uspace.application.cruise.emergencyShuttles.dtos.ShuttleCrewMemberDto;
import uspace.application.cruise.emergencyShuttles.dtos.ShuttleDto;
import uspace.application.cruise.emergencyShuttles.dtos.ShuttleTravelerDto;
import uspace.domain.cruise.exceptions.CruiseNotFoundException;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class EmergencyShuttlesResourceApiComponentTest extends JerseyTest {

    private static final String ANY_CRUISE_ID = "ANY_CRUISE_ID";
    private static final String PATH_CRUISES = "/cruises";
    private static final String PATH_EMERGENCY_SHUTTLES = "/emergencyShuttles";

    private static final ShuttleTravelerDto ANY_TRAVELER = new ShuttleTravelerDto("ANY_TRAVELER_ID", "ANY_TRAVELER_NAME");
    private static final ShuttleCrewMemberDto ANY_CREW_MEMBER = new ShuttleCrewMemberDto("ANY_EMPLOYEE_ID", "ANY_CREW_MEMBER_NAME");
    private static final EmergencyShuttlesDto ANY_EMERGENCY_SHUTTLES_DTO =
            new EmergencyShuttlesDto(150000, List.of(
                    new ShuttleDto("RESCUE_SHIP", List.of(ANY_TRAVELER), List.of(ANY_CREW_MEMBER))
            ));

    private static final String CRUISE_NOT_FOUND_ERROR_CODE = "CRUISE_NOT_FOUND";
    private static final String CRUISE_NOT_FOUND_DESCRIPTION = "Cruise not found.";

    private EmergencyShuttlesService emergencyShuttleServiceMock;

    @Override
    protected Application configure() {
        emergencyShuttleServiceMock = mock(EmergencyShuttlesService.class);

        return new ResourceConfig()
                .register(JacksonFeature.withoutExceptionMappers())
                .register(CruiseNotFoundExceptionMapper.class)
                .register(new EmergencyShuttlesResource(emergencyShuttleServiceMock));
    }

    @Test
    void givenInexistantCruiseId_whenGetEmergencyShuttlesManifest_thenReturnHttp404WithCruiseNotFoundError(){
        doThrow(new CruiseNotFoundException())
                .when(emergencyShuttleServiceMock)
                .getEmergencyShuttlesManifest(ANY_CRUISE_ID);

        Response response = whenGetEmergencyShuttlesWithCruiseId(ANY_CRUISE_ID);
        ErrorResponse errorResponse = response.readEntity(ErrorResponse.class);
        String actual = response.getStatus() + "|" + errorResponse.error + "|" + errorResponse.description;
        String expected = Response.Status.NOT_FOUND.getStatusCode() + "|" + CRUISE_NOT_FOUND_ERROR_CODE + "|" + CRUISE_NOT_FOUND_DESCRIPTION;

        assertEquals(expected, actual);
    }

    @Test
    void givenExistingCruiseId_whenGetEmergencyShuttlesManifest_thenReturnManifestInBody(){
        EmergencyShuttlesDto expectedDto = ANY_EMERGENCY_SHUTTLES_DTO;

        when(emergencyShuttleServiceMock.getEmergencyShuttlesManifest(ANY_CRUISE_ID)).thenReturn(expectedDto);
        Response response = whenGetEmergencyShuttlesWithCruiseId(ANY_CRUISE_ID);
        EmergencyShuttlesDto actualDto = response.readEntity(EmergencyShuttlesDto.class);
        List<String> actualManifestData = new ArrayList<>(actualDto.emergencyShuttles.stream()
                .map(shuttle ->
                        shuttle.type + "|" +
                                shuttle.travelers.stream().map(t -> t.travelerId + "|" + t.travelerName).toList().toString() + "|" +
                                shuttle.crewMembers.stream().map(c -> c.employeeId + "|" + c.crewMemberName).toList().toString())
                .toList());
        actualManifestData.add(actualDto.cost.toString());
        List<String> expectedManifestData = new ArrayList<>(expectedDto.emergencyShuttles.stream()
                .map(shuttle ->
                        shuttle.type + "|" +
                                shuttle.travelers.stream().map(t -> t.travelerId + "|" + t.travelerName).toList().toString() + "|" +
                                shuttle.crewMembers.stream().map(c -> c.employeeId + "|" + c.crewMemberName).toList().toString())
                .toList());
        expectedManifestData.add(expectedDto.cost.toString());

        assertEquals(expectedManifestData, actualManifestData);
    }

    private Response whenGetEmergencyShuttlesWithCruiseId(String cruiseId){
        Invocation.Builder request = target(PATH_CRUISES + "/" + cruiseId + PATH_EMERGENCY_SHUTTLES).request();

        return request.get();
    }

}