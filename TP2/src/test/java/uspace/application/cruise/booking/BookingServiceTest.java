package uspace.application.cruise.booking;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InOrder;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import uspace.application.account.AccountService;
import uspace.application.account.dtos.AccountDto;
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
import uspace.domain.cruise.booking.newBooking.NewBooking;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BookingServiceTest {

    private static final String CRUISE_ID = "JUPITER_MOONS_EXPLORATION_2085";
    private static final String BOOKING_DATE_TIME_STR = "2085-01-17T12:00";
    private static final LocalDateTime BOOKING_DATE_TIME = LocalDateTime.of(2085, 1, 17, 12, 0);
    private static final BigDecimal CALCULATED_COST = new BigDecimal("2999.98");
    private static final String EXPECTED_BOOKING_ID = "ANY_ID";
    private static final String ACCOUNT_USERNAME = "john";

    @Mock private CruiseRepository cruiseRepository;
    @Mock private BookingFactory bookingFactory;
    @Mock private BookingAssembler bookingAssembler;
    @Mock private LocalDateTimeParser localDateTimeParser;
    @Mock private CostCalculator costCalculator;
    @Mock private AccountService accountService;

    @Mock private Cruise cruise;
    @Mock private Booking booking;
    @Mock private NewBooking newBooking;

    private BookingService service;

    @BeforeEach
    void setup() {
        service = new BookingService(
                cruiseRepository,
                bookingFactory,
                bookingAssembler,
                localDateTimeParser,
                costCalculator,
                accountService
        );
    }

    private void arrangeCreate(NewBookingDto dto) {
        dto.accountUsername = ACCOUNT_USERNAME;

        when(accountService.findByUsername(ACCOUNT_USERNAME))
                .thenReturn(new AccountDto(ACCOUNT_USERNAME, "Earth", List.of()));

        when(cruiseRepository.findById(new CruiseId(CRUISE_ID))).thenReturn(cruise);
        when(localDateTimeParser.parse(BOOKING_DATE_TIME_STR)).thenReturn(BOOKING_DATE_TIME);
        when(bookingAssembler.toNewBooking(dto, BOOKING_DATE_TIME)).thenReturn(newBooking);
        when(bookingFactory.create(newBooking)).thenReturn(booking);
        when(costCalculator.calculate(booking, cruise)).thenReturn(CALCULATED_COST);

        BookingId bid = mock(BookingId.class);
        when(booking.getId()).thenReturn(bid);
        when(bid.toString()).thenReturn(EXPECTED_BOOKING_ID);
    }

    @Test
    void createBooking_setsCostSnapshot() {
        NewBookingDto dto = new NewBookingDto();
        dto.bookingDateTime = BOOKING_DATE_TIME_STR;
        arrangeCreate(dto);

        service.createBooking(CRUISE_ID, dto);

        verify(booking).setCost(CALCULATED_COST);
    }

    @Test
    void createBooking_callsProcessBooking() {
        NewBookingDto dto = new NewBookingDto();
        dto.bookingDateTime = BOOKING_DATE_TIME_STR;
        arrangeCreate(dto);

        service.createBooking(CRUISE_ID, dto);

        verify(cruise).processBooking(booking);
    }

    @Test
    void createBooking_callsSave() {
        NewBookingDto dto = new NewBookingDto();
        dto.bookingDateTime = BOOKING_DATE_TIME_STR;
        arrangeCreate(dto);

        service.createBooking(CRUISE_ID, dto);

        verify(cruiseRepository).save(cruise);
    }

    @Test
    void createBooking_returnsId() {
        NewBookingDto dto = new NewBookingDto();
        dto.bookingDateTime = BOOKING_DATE_TIME_STR;
        arrangeCreate(dto);

        BookingIdDto idDto = service.createBooking(CRUISE_ID, dto);

        assertEquals(EXPECTED_BOOKING_ID, idDto.id);
    }

    @Test
    void createBooking_setsCostBeforeSave() {
        NewBookingDto dto = new NewBookingDto();
        dto.bookingDateTime = BOOKING_DATE_TIME_STR;
        arrangeCreate(dto);

        service.createBooking(CRUISE_ID, dto);

        InOrder inOrder = inOrder(booking, cruiseRepository);
        inOrder.verify(booking).setCost(CALCULATED_COST);
        inOrder.verify(cruiseRepository).save(cruise);
    }

    @Test
    void findBooking_returnsAssemblerDto() {
        String bookingIdStr = "BID";
        BookingId bid = new BookingId(bookingIdStr);
        BookingDto dto = mock(BookingDto.class);
        when(cruiseRepository.findById(new CruiseId(CRUISE_ID))).thenReturn(cruise);
        when(cruise.findBookingById(bid)).thenReturn(booking);
        when(bookingAssembler.toDto(CRUISE_ID, booking)).thenReturn(dto);

        BookingDto result = service.findBooking(CRUISE_ID, bookingIdStr);

        assertEquals(dto, result);
    }
}
