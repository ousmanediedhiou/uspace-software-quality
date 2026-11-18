package uspace.application.cruise.booking;

import jakarta.inject.Inject;
import uspace.application.account.AccountService;
import uspace.application.cruise.booking.dtos.BookingDto;
import uspace.application.cruise.booking.dtos.BookingIdDto;
import uspace.application.cruise.booking.dtos.newBooking.NewBookingDto;
import uspace.application.utils.dateTimeParser.LocalDateTimeParser;
import uspace.domain.cruise.Cruise;
import uspace.domain.cruise.CruiseId;
import uspace.domain.cruise.CruiseRepository;
import uspace.domain.cruise.booking.Booking;
import uspace.domain.cruise.booking.BookingFactory;
import uspace.domain.cruise.booking.BookingId;
import uspace.domain.cruise.booking.CostCalculator;
import uspace.domain.cruise.booking.exceptions.BookingNotFoundException;
import uspace.domain.cruise.booking.newBooking.NewBooking;
import uspace.domain.cruise.exceptions.CruiseNotFoundException;
import uspace.domain.exceptions.MissingParameterException;

import java.time.LocalDateTime;

public class BookingService {

    private final CruiseRepository cruiseRepository;
    private final BookingFactory bookingFactory;
    private final BookingAssembler bookingAssembler;
    private final LocalDateTimeParser localDateTimeParser;
    private final CostCalculator costCalculator;
    private final AccountService accountService;

    @Inject
    public BookingService(CruiseRepository cruiseRepository,
                          BookingFactory bookingFactory,
                          BookingAssembler bookingAssembler,
                          LocalDateTimeParser localDateTimeParser,
                          CostCalculator costCalculator,
                          AccountService accountService) {
        this.cruiseRepository = cruiseRepository;
        this.bookingFactory = bookingFactory;
        this.bookingAssembler = bookingAssembler;
        this.localDateTimeParser = localDateTimeParser;
        this.costCalculator = costCalculator;
        this.accountService = accountService;
    }

    public BookingIdDto createBooking(String cruiseId, NewBookingDto newBookingDto) {
        if (newBookingDto == null || newBookingDto.accountUsername == null || newBookingDto.accountUsername.isBlank()) {
            throw new MissingParameterException("accountUsername");
        }

        accountService.findByUsername(newBookingDto.accountUsername);

        Cruise cruise = findCruiseById(cruiseId);
        LocalDateTime bookingDateTime = localDateTimeParser.parse(newBookingDto.bookingDateTime);
        NewBooking newBooking = bookingAssembler.toNewBooking(newBookingDto, bookingDateTime);
        Booking booking = bookingFactory.create(newBooking);
        booking.setCost(costCalculator.calculate(booking, cruise));
        cruise.processBooking(booking);
        cruiseRepository.save(cruise);

        String bookingId = booking.getId().toString();
        accountService.addBookingToAccount(newBookingDto.accountUsername, bookingId);
        return new BookingIdDto(bookingId);
    }

    public BookingDto findBooking(String cruiseId, String bookingIdStr) {
        Cruise cruise = findCruiseById(cruiseId);
        BookingId bookingId = new BookingId(bookingIdStr);
        Booking booking = cruise.findBookingById(bookingId);
        if (booking == null) throw new BookingNotFoundException();
        return bookingAssembler.toDto(cruiseId, booking);
    }

    private Cruise findCruiseById(String cruiseId) {
        Cruise cruise = cruiseRepository.findById(new CruiseId(cruiseId));
        if (cruise == null) throw new CruiseNotFoundException();
        return cruise;
    }
}
