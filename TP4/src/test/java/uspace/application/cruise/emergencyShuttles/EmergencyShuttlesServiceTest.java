package uspace.application.cruise.emergencyShuttles;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.function.Executable;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import uspace.application.cruise.emergencyShuttles.dtos.EmergencyShuttlesDto;
import uspace.application.cruise.emergencyShuttles.dtos.ShuttleCrewMemberDto;
import uspace.application.cruise.emergencyShuttles.dtos.ShuttleDto;
import uspace.application.cruise.emergencyShuttles.dtos.ShuttleTravelerDto;
import uspace.domain.cruise.Cruise;
import uspace.domain.cruise.CruiseId;
import uspace.domain.cruise.CruiseRepository;
import uspace.domain.cruise.booking.Booking;
import uspace.domain.cruise.booking.traveler.*;
import uspace.domain.cruise.crew.crewMember.CrewMember;
import uspace.domain.cruise.crew.crewMember.CrewMemberName;
import uspace.domain.cruise.crew.crewMember.employeeId.EmployeeId;
import uspace.domain.cruise.emergencyShuttles.EmergencyShuttles;
import uspace.domain.cruise.emergencyShuttles.ShuttleType;
import uspace.domain.cruise.emergencyShuttles.ShuttlesConfiguration;
import uspace.domain.cruise.exceptions.CruiseNotFoundException;
import uspace.infra.emergencyShuttles.dtos.ShuttleConfigurationDto;


import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class EmergencyShuttlesServiceTest {

    private static final String ANY_CRUISE_ID_STR = "ANY_CRUISE_ID";
    private static final CruiseId ANY_CRUISE_ID = new CruiseId(ANY_CRUISE_ID_STR);
    private static final EmergencyShuttlesDto ANY_EMERGENCY_SHUTTLES_DTO = new EmergencyShuttlesDto(0, List.of());
    private static final ShuttleTravelerDto ANY_TRAVELER = new ShuttleTravelerDto("ANY_TRAVELER_ID", "ANY_TRAVELER_NAME");
    private static final ShuttleCrewMemberDto ANY_CREW_MEMBER = new ShuttleCrewMemberDto("ANY_EMPLOYEE_ID", "ANY_CREW_MEMBER_NAME");
    private static final EmergencyShuttlesDto ANY_OTHER_EMERGENCY_SHUTTLES_DTO =
            new EmergencyShuttlesDto(150000, List.of(
                    new ShuttleDto("RESCUE_SHIP", List.of(ANY_TRAVELER), List.of(ANY_CREW_MEMBER))
            ));
    private static final ShuttleConfigurationDto ANY_SHUTTLE_CONFIGURATION =
            new ShuttleConfigurationDto("RESCUE_SHIP",50,5, 150000, false);
    private static final ShuttleConfigurationDto ANY_OTHER_SHUTTLE_CONFIGURATION =
            new ShuttleConfigurationDto("STANDARD_SHUTTLE",20,-1, 50000, true);
    private static final EmergencyShuttles ANY_EMERGENCY_SHUTTLES = new EmergencyShuttles();

    private EmergencyShuttlesService emergencyShuttlesService;

    @Mock
    private CruiseRepository cruiseRepositoryMock;
    @Mock
    private EmergencyShuttlesAssembler emergencyShuttlesAssemblerMock;
    @Mock
    private ShuttlesConfiguration shuttlesConfigurationMock;
    @Mock
    private Cruise cruiseMock;
    @Mock
    private Booking bookingMock;

    @BeforeEach
    void createEmergencyShuttlesService(){
        emergencyShuttlesService = new EmergencyShuttlesService(
                cruiseRepositoryMock,
                emergencyShuttlesAssemblerMock,
                shuttlesConfigurationMock);
    }

    @Test
    void givenNonExistingCruise_whenGetEmergencyShuttlesManifest_thenThrowCruiseNotFoundException() {
        when(cruiseRepositoryMock.findById(ANY_CRUISE_ID)).thenReturn(null);

        Executable getEmergencyShuttlesManifest = () ->
                emergencyShuttlesService.getEmergencyShuttlesManifest(ANY_CRUISE_ID_STR);

        assertThrows(CruiseNotFoundException.class, getEmergencyShuttlesManifest);
    }

    @Test
    void givenExistingEmptyCruise_whenGetEmergencyShuttlesManifest_thenReturnsEmptyManifest() {
        givenExistingCruise();
        givenShuttlesConfiguration();
        givenTravelersAndCrewMembers(List.of(), List.of());
        givenEmergencyShuttleAssemblerResult_EmptyManifest();

        EmergencyShuttlesDto emergencyShuttlesDto =
                emergencyShuttlesService.getEmergencyShuttlesManifest(ANY_CRUISE_ID_STR);

        assertSame(ANY_EMERGENCY_SHUTTLES_DTO, emergencyShuttlesDto);
    }

    @Test
    void givenExistingCruiseWithPeople_whenGetEmergencyShuttlesManifest_thenReturnsEmptyManifest() {
        givenExistingCruise();
        givenShuttlesConfiguration();
        givenTravelersAndCrewMembers(
                List.of(
                        new AdultTraveler(
                                new TravelerId(ANY_TRAVELER.travelerId),
                                new TravelerName(ANY_TRAVELER.travelerName),
                                List.of(),
                                new StandardCostMultiplier(1),
                                List.of())
                ),
                List.of(new CrewMember(new EmployeeId(ANY_CREW_MEMBER.employeeId), new CrewMemberName(ANY_CREW_MEMBER.crewMemberName)))
        );
        givenEmergencyShuttleAssemblerResult();

        EmergencyShuttlesDto emergencyShuttlesDto =
                emergencyShuttlesService.getEmergencyShuttlesManifest(ANY_CRUISE_ID_STR);

        assertSame(ANY_OTHER_EMERGENCY_SHUTTLES_DTO, emergencyShuttlesDto);
    }

    private void givenExistingCruise() {
        when(cruiseRepositoryMock.findById(ANY_CRUISE_ID)).thenReturn(cruiseMock);
        when(cruiseMock.getEmergencyShuttles()).thenReturn(ANY_EMERGENCY_SHUTTLES);
    }

    private void givenRescueShipConfiguration(){
        when(shuttlesConfigurationMock.getShuttleConfigurationByType(ShuttleType.RESCUE_SHIP))
                .thenReturn(ANY_SHUTTLE_CONFIGURATION);
    }

    private void givenStandardShuttleConfiguration(){
        when(shuttlesConfigurationMock.getShuttleConfigurationByType(ShuttleType.STANDARD_SHUTTLE))
                .thenReturn(ANY_OTHER_SHUTTLE_CONFIGURATION);
    }

    private void givenShuttlesConfiguration(){
        givenRescueShipConfiguration();
        givenStandardShuttleConfiguration();
    }

    private void givenTravelersAndCrewMembers(List<Traveler> travelers, List<CrewMember> crewMembers) {
        when(cruiseMock.getBookings()).thenReturn(List.of(bookingMock));
        when(bookingMock.getTravelers()).thenReturn(travelers);
        when(cruiseMock.getCrewMembers()).thenReturn(crewMembers);
    }

    private void givenEmergencyShuttleAssemblerResult_EmptyManifest(){
        when(emergencyShuttlesAssemblerMock.toDto(any()))
                .thenReturn(ANY_EMERGENCY_SHUTTLES_DTO);
    }

    private void givenEmergencyShuttleAssemblerResult(){
        when(emergencyShuttlesAssemblerMock.toDto(any()))
                .thenReturn(ANY_OTHER_EMERGENCY_SHUTTLES_DTO);
    }
}