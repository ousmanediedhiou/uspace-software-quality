package uspace.application.cruise.crew;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.function.Executable;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import uspace.application.cruise.crew.dtos.CrewMemberDto;
import uspace.domain.cruise.Cruise;
import uspace.domain.cruise.CruiseId;
import uspace.domain.cruise.CruiseRepository;
import uspace.domain.cruise.cabin.CabinType;
import uspace.domain.cruise.cabin.exceptions.CabinTypeNotAvailableException;
import uspace.domain.cruise.crew.CrewMember;
import uspace.domain.cruise.crew.exceptions.CrewMemberAlreadyInCruiseException;
import uspace.domain.cruise.crew.exceptions.InvalidEmployeeIdFormatException;
import uspace.domain.cruise.exceptions.CruiseNotFoundException;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CrewMemberServiceTest {

    private static final String ANY_CRUISE_ID = "ANY_CRUISE_ID";
    private static final CruiseId ANY_CRUISE_ID_VO = new CruiseId(ANY_CRUISE_ID);
    private static final String VALID_EMPLOYEE_ID = "ABC123";
    private static final String INVALID_EMPLOYEE_ID = "abc123";
    private static final String ANY_NAME = "John Doe";

    @Mock private CruiseRepository cruiseRepository;
    @Mock private CabinAllocationPolicy cabinAllocationPolicy;
    @Mock private Cruise cruise;

    private CrewMemberService service;

    @BeforeEach
    void setUp() {
        service = new CrewMemberService(cruiseRepository, cabinAllocationPolicy);
    }

    private static CrewMemberDto dto(String name, String employeeId) {
        CrewMemberDto d = new CrewMemberDto();
        d.name = name;
        d.employeeId = employeeId;
        return d;
    }


    @Test
    void givenUnknownCruise_whenAddCrewMember_thenThrowCruiseNotFoundException() {
        when(cruiseRepository.findById(ANY_CRUISE_ID_VO)).thenReturn(null);
        CrewMemberDto request = dto(ANY_NAME, VALID_EMPLOYEE_ID);

        Executable add = () -> service.addCrewMember(ANY_CRUISE_ID, request);

        assertThrows(CruiseNotFoundException.class, add);
        verify(cruiseRepository).findById(ANY_CRUISE_ID_VO);
        verifyNoMoreInteractions(cruiseRepository, cabinAllocationPolicy, cruise);
    }

    @Test
    void givenInvalidEmployeeIdFormat_whenAddCrewMember_thenThrowInvalidEmployeeIdFormatException() {
        when(cruiseRepository.findById(ANY_CRUISE_ID_VO)).thenReturn(cruise);
        CrewMemberDto request = dto(ANY_NAME, INVALID_EMPLOYEE_ID);

        Executable add = () -> service.addCrewMember(ANY_CRUISE_ID, request);

        assertThrows(InvalidEmployeeIdFormatException.class, add);
        verify(cruiseRepository).findById(ANY_CRUISE_ID_VO);
        verifyNoInteractions(cabinAllocationPolicy);
        verifyNoMoreInteractions(cruiseRepository, cruise);
    }

    @Test
    void givenExistingEmployeeId_whenAddCrewMember_thenThrowCrewMemberAlreadyInCruiseException() {
        when(cruiseRepository.findById(ANY_CRUISE_ID_VO)).thenReturn(cruise);
        CrewMember existing = mock(CrewMember.class);
        when(existing.getEmployeeId()).thenReturn(VALID_EMPLOYEE_ID);
        when(cruise.getCrewMembers()).thenReturn(List.of(existing));
        CrewMemberDto request = dto(ANY_NAME, VALID_EMPLOYEE_ID);

        Executable add = () -> service.addCrewMember(ANY_CRUISE_ID, request);

        assertThrows(CrewMemberAlreadyInCruiseException.class, add);
        verify(cruiseRepository).findById(ANY_CRUISE_ID_VO);
        verify(cruise).getCrewMembers();
        verifyNoInteractions(cabinAllocationPolicy);
        verifyNoMoreInteractions(cruiseRepository, cruise);
    }

    @Test
    void givenNoStandardCabinAvailable_whenAddCrewMember_thenThrowCabinTypeNotAvailableException() {
        when(cruiseRepository.findById(ANY_CRUISE_ID_VO)).thenReturn(cruise);
        when(cruise.getCrewMembers()).thenReturn(List.of());
        CrewMemberDto request = dto(ANY_NAME, VALID_EMPLOYEE_ID);

        doThrow(new CabinTypeNotAvailableException())
                .when(cabinAllocationPolicy).allocateForCrew(cruise, CabinType.STANDARD);

        Executable add = () -> service.addCrewMember(ANY_CRUISE_ID, request);

        assertThrows(CabinTypeNotAvailableException.class, add);
        verify(cruiseRepository).findById(ANY_CRUISE_ID_VO);
        verify(cruise).getCrewMembers();
        verify(cabinAllocationPolicy).allocateForCrew(cruise, CabinType.STANDARD);
        verifyNoMoreInteractions(cruiseRepository, cabinAllocationPolicy, cruise);
    }


    @Test
    void givenValidInputs_whenAddCrewMember_thenAllocateStandardCabinViaPolicy() {
        when(cruiseRepository.findById(ANY_CRUISE_ID_VO)).thenReturn(cruise);
        when(cruise.getCrewMembers()).thenReturn(List.of());
        CrewMemberDto request = dto(ANY_NAME, VALID_EMPLOYEE_ID);

        service.addCrewMember(ANY_CRUISE_ID, request);

        verify(cabinAllocationPolicy, times(1)).allocateForCrew(cruise, CabinType.STANDARD);
        verifyNoMoreInteractions(cabinAllocationPolicy);
    }

    @Test
    void givenValidInputs_whenAddCrewMember_thenAddCrewMemberWithCorrectEmployeeId() {
        when(cruiseRepository.findById(ANY_CRUISE_ID_VO)).thenReturn(cruise);
        when(cruise.getCrewMembers()).thenReturn(List.of());
        CrewMemberDto request = dto(ANY_NAME, VALID_EMPLOYEE_ID);

        service.addCrewMember(ANY_CRUISE_ID, request);

        ArgumentCaptor<CrewMember> captor = ArgumentCaptor.forClass(CrewMember.class);
        verify(cruise, times(1)).addCrewMember(captor.capture());
        assertEquals(VALID_EMPLOYEE_ID, captor.getValue().getEmployeeId());
        verifyNoMoreInteractions(cruise);
    }

    @Test
    void givenValidInputs_whenAddCrewMember_thenPersistCruiseExactlyOnce() {
        when(cruiseRepository.findById(ANY_CRUISE_ID_VO)).thenReturn(cruise);
        when(cruise.getCrewMembers()).thenReturn(List.of());
        CrewMemberDto request = dto(ANY_NAME, VALID_EMPLOYEE_ID);

        service.addCrewMember(ANY_CRUISE_ID, request);

        verify(cruiseRepository, times(1)).save(cruise);
        verifyNoMoreInteractions(cruiseRepository);
    }
}
