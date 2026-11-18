package uspace.domain.cruise.emergencyShuttles;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import uspace.domain.cruise.crew.crewMember.CrewMemberName;
import uspace.domain.cruise.crew.crewMember.employeeId.EmployeeId;

@Entity
public class ShuttleCrewMember {
    @Id
    private EmployeeId employeeId;

    private CrewMemberName crewMemberName;

    public ShuttleCrewMember() {
    }

    public ShuttleCrewMember(EmployeeId employeeId, CrewMemberName crewMemberName) {
        this.employeeId = employeeId;
        this.crewMemberName = crewMemberName;
    }

    public EmployeeId getEmployeeId() {
        return employeeId;
    }

    public CrewMemberName getName() {
        return crewMemberName;
    }

}
