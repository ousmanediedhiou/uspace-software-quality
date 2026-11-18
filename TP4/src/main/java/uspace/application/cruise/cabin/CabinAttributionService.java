package uspace.application.cruise.cabin;

import jakarta.inject.Inject;
import uspace.application.cruise.cabin.dtos.CabinAttributionsDto;
import uspace.domain.cruise.Cruise;
import uspace.domain.cruise.CruiseId;
import uspace.domain.cruise.CruiseRepository;
import uspace.domain.cruise.booking.Booking;
import uspace.domain.cruise.cabin.CabinConfiguration;
import uspace.domain.cruise.cabin.CabinType;
import uspace.domain.cruise.exceptions.CruiseNotFoundException;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class CabinAttributionService {

    private final CruiseRepository cruiseRepository;
    private final CabinConfiguration cabinConfiguration;
    private final CabinAttributionEngine cabinAttributionEngine;
    private final CabinAttributionAssembler cabinAttributionAssembler;

    @Inject
    public CabinAttributionService(CruiseRepository cruiseRepository,
                                   CabinConfiguration cabinConfiguration,
                                   CabinAttributionEngine cabinAttributionEngine,
                                   CabinAttributionAssembler cabinAttributionAssembler) {
        this.cruiseRepository = cruiseRepository;
        this.cabinConfiguration = cabinConfiguration;
        this.cabinAttributionEngine = cabinAttributionEngine;
        this.cabinAttributionAssembler = cabinAttributionAssembler;
    }

    public CabinAttributionsDto getCabinAttributions(String cruiseId, String criteriaString) {
        Cruise cruise = cruiseRepository.findById(new CruiseId(cruiseId));
        if (cruise == null) {
            throw new CruiseNotFoundException();
        }

        CabinAttributionCriteria criteria = CabinAttributionCriteria.fromString(criteriaString);

        List<CabinAttributionBooking> bookingInfos = createCabinAttributionBookings(cruise.getBookings());
        Map<CabinType, List<String>> cabinsByType = cabinConfiguration.findCabinIdsByType();

        List<CabinAttributionResult> results =
                cabinAttributionEngine.assignCabins(bookingInfos, cabinsByType, criteria);

        return cabinAttributionAssembler.toDto(results);
    }

    private List<CabinAttributionBooking> createCabinAttributionBookings(List<Booking> bookings) {
        List<CabinAttributionBooking> bookingInfos = new ArrayList<>();

        for (Booking booking : bookings) {
            CabinAttributionBooking bookingInfo = new CabinAttributionBooking(
                    booking.getId(),
                    booking.getCabinType(),
                    booking.getTravelers().size(),
                    booking.getBookingDateTime());

            bookingInfos.add(bookingInfo);
        }

        return bookingInfos;
    }
}
