package uspace.application.cruise.crew;

import jakarta.inject.Inject;
import uspace.application.cruise.crew.dtos.CrewMemberDto;
import uspace.domain.cruise.Cruise;
import uspace.domain.cruise.CruiseId;
import uspace.domain.cruise.CruiseRepository;
import uspace.domain.cruise.booking.traveler.TravelerId;
import uspace.domain.cruise.booking.traveler.TravelerName;
import uspace.domain.cruise.cabin.CabinType;
import uspace.domain.cruise.crew.CrewMember;
import uspace.domain.cruise.crew.exceptions.CrewMemberAlreadyInCruiseException;
import uspace.domain.cruise.crew.exceptions.InvalidEmployeeIdFormatException;
import uspace.domain.cruise.exceptions.CruiseNotFoundException;

public class CrewMemberService {

    private final CruiseRepository cruiseRepository;
    private final CabinAllocationPolicy cabinAllocationPolicy;

    @Inject
    public CrewMemberService(CruiseRepository cruiseRepository, CabinAllocationPolicy cabinAllocationPolicy) {
        this.cruiseRepository = cruiseRepository;
        this.cabinAllocationPolicy = cabinAllocationPolicy;
    }

    public void addCrewMember(String cruiseId, CrewMemberDto crewMemberDto) {
        Cruise cruise = cruiseRepository.findById(new CruiseId(cruiseId));
        if (cruise == null) throw new CruiseNotFoundException();
        if (crewMemberDto.employeeId == null || !crewMemberDto.employeeId.matches("[A-Z]{3}\\d{3}"))
            throw new InvalidEmployeeIdFormatException();

        boolean alreadyExists = cruise.getCrewMembers().stream()
                .anyMatch(cm -> crewMemberDto.employeeId.equals(cm.getEmployeeId()));
        if (alreadyExists) throw new CrewMemberAlreadyInCruiseException();

        cabinAllocationPolicy.allocateForCrew(cruise, CabinType.STANDARD);

        CrewMember newCrewMember = new CrewMember(
                new TravelerId(),
                crewMemberDto.employeeId,
                new TravelerName(crewMemberDto.name)
        );
        newCrewMember.setCruise(cruise);

        cruise.addCrewMember(newCrewMember);
        cruiseRepository.save(cruise);
    }
}
