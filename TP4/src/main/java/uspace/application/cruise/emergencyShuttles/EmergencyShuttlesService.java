package uspace.application.cruise.emergencyShuttles;

import jakarta.inject.Inject;
import uspace.application.cruise.emergencyShuttles.dtos.EmergencyShuttlesDto;
import uspace.domain.cruise.Cruise;
import uspace.domain.cruise.CruiseId;
import uspace.domain.cruise.CruiseRepository;
import uspace.domain.cruise.booking.Booking;
import uspace.domain.cruise.booking.traveler.Traveler;
import uspace.domain.cruise.crew.crewMember.CrewMember;
import uspace.domain.cruise.emergencyShuttles.EmergencyShuttles;
import uspace.domain.cruise.emergencyShuttles.Shuttle;
import uspace.domain.cruise.emergencyShuttles.ShuttleType;
import uspace.domain.cruise.emergencyShuttles.ShuttlesConfiguration;
import uspace.domain.cruise.exceptions.CruiseNotFoundException;
import uspace.domain.cruise.money.Money;
import uspace.infra.emergencyShuttles.dtos.ShuttleConfigurationDto;

import java.util.ArrayList;
import java.util.List;

public class EmergencyShuttlesService {

    private final CruiseRepository cruiseRepository;
    private final EmergencyShuttlesAssembler emergencyShuttlesAssembler;
    private final ShuttlesConfiguration shuttlesConfiguration;

    @Inject
    public EmergencyShuttlesService(CruiseRepository cruiseRepository, EmergencyShuttlesAssembler emergencyShuttlesAssembler, ShuttlesConfiguration shuttlesConfiguration) {
        this.cruiseRepository = cruiseRepository;
        this.emergencyShuttlesAssembler = emergencyShuttlesAssembler;
        this.shuttlesConfiguration = shuttlesConfiguration;
    }

    public EmergencyShuttlesDto getEmergencyShuttlesManifest(String cruiseId){
        Cruise cruise = findCruiseById(cruiseId);

        EmergencyShuttles emergencyShuttles = cruise.getEmergencyShuttles();
        processCrewMemberShuttleAttribution(emergencyShuttles, cruise);
        processTravelersShuttleAttribution(emergencyShuttles, cruise);

        EmergencyShuttlesDto emergencyShuttlesDto =  emergencyShuttlesAssembler.toDto(emergencyShuttles);

        return emergencyShuttlesDto;
    }

    private Cruise findCruiseById(String cruiseId) {
        Cruise cruise = cruiseRepository.findById(new CruiseId(cruiseId));
        if (cruise == null) {
            throw new CruiseNotFoundException();
        }
        return cruise;
    }

    private Shuttle createNewEmptyShuttleUsingConfig(ShuttleConfigurationDto config){
        return new Shuttle(
                new Money(config.cost),
                ShuttleType.fromString(config.type),
                new ArrayList<>(),
                new ArrayList<>(),
                config.capacity
        );
    }

    private List<Traveler> getAllTravelers(Cruise cruise){
        List<Traveler> travelers = new ArrayList<>();

        for (Booking booking : cruise.getBookings()){
            for (Traveler traveler : booking.getTravelers()){
                if( (!travelers.contains(traveler)) ){
                    travelers.add(traveler);
                }
            }
        }

        return travelers;
    }

    private void processCrewMemberShuttleAttribution(EmergencyShuttles emergencyShuttles, Cruise cruise){
        ShuttleConfigurationDto rescueShipConfig = shuttlesConfiguration.getShuttleConfigurationByType(ShuttleType.RESCUE_SHIP);
        ShuttleConfigurationDto standardShuttleConfig = shuttlesConfiguration.getShuttleConfigurationByType(ShuttleType.STANDARD_SHUTTLE);

        for( CrewMember crewMember : cruise.getCrewMembers() ){
            Shuttle shuttleToFill = emergencyShuttles.findFirstNonFullShuttle(ShuttleType.RESCUE_SHIP);
            if(shuttleToFill == null){
                if( rescueShipConfig.unlimited || (emergencyShuttles.numberOfShuttlesOfType(ShuttleType.RESCUE_SHIP) < rescueShipConfig.amount) ){
                    shuttleToFill = createNewEmptyShuttleUsingConfig(rescueShipConfig);
                    shuttleToFill.addCrewMember(crewMember);
                    emergencyShuttles.addShuttle(shuttleToFill);
                } else {
                    shuttleToFill = emergencyShuttles.findFirstNonFullShuttle(ShuttleType.STANDARD_SHUTTLE);
                    if(shuttleToFill == null){
                        shuttleToFill = createNewEmptyShuttleUsingConfig(standardShuttleConfig);
                        shuttleToFill.addCrewMember(crewMember);
                        emergencyShuttles.addShuttle(shuttleToFill);
                    } else{
                        shuttleToFill.addCrewMember(crewMember);
                    }
                }
            } else{
                shuttleToFill.addCrewMember(crewMember);
            }
        }
    }

    private void processTravelersShuttleAttribution(EmergencyShuttles emergencyShuttles, Cruise cruise){
        ShuttleConfigurationDto rescueShipConfig = shuttlesConfiguration.getShuttleConfigurationByType(ShuttleType.RESCUE_SHIP);
        ShuttleConfigurationDto standardShuttleConfig = shuttlesConfiguration.getShuttleConfigurationByType(ShuttleType.STANDARD_SHUTTLE);

        for( Traveler traveler : getAllTravelers(cruise) ){
            Shuttle shuttleToFill = emergencyShuttles.findFirstNonFullShuttle(ShuttleType.RESCUE_SHIP);
            if(shuttleToFill == null){
                if( rescueShipConfig.unlimited || (emergencyShuttles.numberOfShuttlesOfType(ShuttleType.RESCUE_SHIP) < rescueShipConfig.amount) ){
                    shuttleToFill = createNewEmptyShuttleUsingConfig(rescueShipConfig);
                    shuttleToFill.addTraveler(traveler);
                    emergencyShuttles.addShuttle(shuttleToFill);
                } else {
                    shuttleToFill = emergencyShuttles.findFirstNonFullShuttle(ShuttleType.STANDARD_SHUTTLE);
                    if(shuttleToFill == null){
                        shuttleToFill = createNewEmptyShuttleUsingConfig(standardShuttleConfig);
                        shuttleToFill.addTraveler(traveler);
                        emergencyShuttles.addShuttle(shuttleToFill);
                    } else{
                        shuttleToFill.addTraveler(traveler);
                    }
                }
            } else{
                shuttleToFill.addTraveler(traveler);
            }
        }
    }
}
