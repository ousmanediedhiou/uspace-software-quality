package uspace.domain.cruise.booking;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.function.Executable;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import uspace.application.account.AccountService;
import uspace.application.account.dtos.AccountDto;
import uspace.application.cruise.booking.BookingAssembler;
import uspace.application.cruise.booking.BookingService;
import uspace.application.cruise.booking.dtos.newBooking.NewBookingDto;
import uspace.application.utils.dateTimeParser.LocalDateTimeParser;
import uspace.domain.cruise.Cruise;
import uspace.domain.cruise.CruiseId;
import uspace.domain.cruise.CruiseRepository;
import uspace.domain.cruise.booking.newBooking.NewBooking;
import uspace.domain.exceptions.MissingParameterException;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BookingServiceAccountRulesTest {

    private static final String CRUISE_ID = "JUPITER_MOONS_EXPLORATION_2085";
    private static final String DATE_STR = "2085-01-15T12:00";
    private static final LocalDateTime DATE = LocalDateTime.of(2085, 1, 15, 12, 0);
    private static final BigDecimal COST = new BigDecimal("1000.00");
    private static final String USERNAME = "john";

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
                cruiseRepository, bookingFactory, bookingAssembler, localDateTimeParser, costCalculator, accountService
        );
    }

    private NewBookingDto validDto() {
        NewBookingDto dto = new NewBookingDto("STANDARD", List.of(), DATE_STR);
        dto.accountUsername = USERNAME;
        return dto;
    }

    private void arrangeHappyPath(NewBookingDto dto) {
        when(cruiseRepository.findById(new CruiseId(CRUISE_ID))).thenReturn(cruise);
        when(localDateTimeParser.parse(DATE_STR)).thenReturn(DATE);
        when(bookingAssembler.toNewBooking(dto, DATE)).thenReturn(newBooking);
        when(bookingFactory.create(newBooking)).thenReturn(booking);
        when(costCalculator.calculate(booking, cruise)).thenReturn(COST);
        when(accountService.findByUsername(USERNAME)).thenReturn(new AccountDto(USERNAME, "Earth", List.of()));
        when(booking.getId()).thenReturn(new uspace.domain.cruise.booking.BookingId("ID"));
    }

    @Test
    void createBooking_whenAccountUsernameMissing_thenThrowMissingParameterException() {
        NewBookingDto dto = new NewBookingDto("STANDARD", List.of(), DATE_STR);
        dto.accountUsername = null;

        Executable call = () -> service.createBooking(CRUISE_ID, dto);

        assertThrows(MissingParameterException.class, call);
    }

    @Test
    void createBooking_whenAccountNotFound_thenThrowAccountNotFoundException() {
        NewBookingDto dto = validDto();
        when(accountService.findByUsername(USERNAME)).thenThrow(new uspace.application.account.AccountNotFoundException());

        Executable call = () -> service.createBooking(CRUISE_ID, dto);

        assertThrows(uspace.application.account.AccountNotFoundException.class, call);
    }

    @Test
    void createBooking_whenValid_thenAccountServiceIsUsed() {
        NewBookingDto dto = validDto();
        arrangeHappyPath(dto);

        service.createBooking(CRUISE_ID, dto);

        verify(accountService).findByUsername(USERNAME);
    }
}
