package uspace.api.cruise.booking.traveler;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.ws.rs.client.Entity;
import jakarta.ws.rs.core.Application;
import jakarta.ws.rs.core.Response;
import org.glassfish.hk2.utilities.binding.AbstractBinder;
import org.glassfish.jersey.jackson.JacksonFeature;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.test.JerseyTest;
import org.junit.jupiter.api.Test;
import uspace.api.ConfigurationServerRest;
import uspace.application.cruise.booking.traveler.TravelerService;
import uspace.domain.cruise.booking.traveler.exceptions.TravelerNotFoundException;
import uspace.domain.cruise.exceptions.CruiseNotFoundException;
import uspace.domain.cruise.sport.exceptions.SportAlreadyBookedException;
import uspace.domain.cruise.sport.exceptions.SportNotBookableException;
import uspace.domain.cruise.sport.exceptions.SportNotFoundException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

class TravelerResourceApiComponentTest extends JerseyTest {

    private static final String ANY_CRUISE_ID = "ANY_CRUISE_ID";
    private static final String ANY_BOOKING_ID = "ANY_BOOKING_ID";
    private static final String ANY_TRAVELER_ID = "ANY_TRAVELER_ID";
    private static final String ANY_SPORT_NAME = "Lunar Soccer";

    private static final String EMPTY_JSON_OBJECT = "{}";

    private static final String SPORT_FIELD = "sport";
    private static final String ERROR_FIELD = "error";
    private static final String DESCRIPTION_FIELD = "description";

    private static final String MISSING_PARAMETER_ERROR = "MISSING_PARAMETER";
    private static final String MISSING_SPORT_DESCRIPTION = "Missing parameter: sport";

    private static final String CRUISE_NOT_FOUND_ERROR = "CRUISE_NOT_FOUND";
    private static final String CRUISE_NOT_FOUND_DESCRIPTION = "Cruise not found";

    private static final String TRAVELER_NOT_FOUND_ERROR = "TRAVELER_NOT_FOUND";
    private static final String TRAVELER_NOT_FOUND_DESCRIPTION = "Traveler not found";

    private static final String SPORT_NOT_FOUND_ERROR = "SPORT_NOT_FOUND";
    private static final String SPORT_NOT_FOUND_DESCRIPTION = "Sport not found";

    private static final String SPORT_NOT_BOOKABLE_ERROR = "SPORT_NOT_BOOKABLE";
    private static final String SPORT_NOT_BOOKABLE_DESCRIPTION = "Sport not bookable for traveler";

    private static final String SPORT_ALREADY_BOOKED_ERROR = "SPORT_ALREADY_BOOKED";
    private static final String SPORT_ALREADY_BOOKED_DESCRIPTION = "Sport already booked by traveler";

    private final ObjectMapper objectMapper = new ObjectMapper();

    private TravelerService travelerServiceMock;

    @Override
    protected Application configure() {
        travelerServiceMock = mock(TravelerService.class);

        ConfigurationServerRest.useInMemoryRepositories();

        return new ResourceConfig()
                .packages("uspace")
                .register(JacksonFeature.withoutExceptionMappers())
                .register(ConfigurationServerRest.class)
                .register(new AbstractBinder() {
                    @Override
                    protected void configure() {
                        bind(travelerServiceMock).to(TravelerService.class);
                    }
                });
    }

    private String sportsPath() {
        return "/cruises/" + ANY_CRUISE_ID
                + "/bookings/" + ANY_BOOKING_ID
                + "/travelers/" + ANY_TRAVELER_ID
                + "/sports";
    }

    private String createSportRequestBody(String sportName) {
        return "{\"" + SPORT_FIELD + "\":\"" + sportName + "\"}";
    }

    private JsonNode readJson(Response response) {
        try {
            String body = response.readEntity(String.class);
            return objectMapper.readTree(body);
        } catch (Exception e) {
            throw new RuntimeException("Unable to read JSON response", e);
        }
    }

    @Test
    void givenValidRequest_whenBookSport_thenReturnHttp200AndDelegateToService() {
        String body = createSportRequestBody(ANY_SPORT_NAME);

        Response response = target(sportsPath())
                .request()
                .post(Entity.json(body));

        assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());

        verify(travelerServiceMock)
                .bookSport(ANY_CRUISE_ID, ANY_BOOKING_ID, ANY_TRAVELER_ID, ANY_SPORT_NAME);
    }

    @Test
    void givenMissingSportParameter_whenBookSport_thenReturn400WithMissingParameterError() {
        String bodyWithoutSport = EMPTY_JSON_OBJECT;

        Response response = target(sportsPath())
                .request()
                .post(Entity.json(bodyWithoutSport));

        assertEquals(Response.Status.BAD_REQUEST.getStatusCode(), response.getStatus());

        JsonNode json = readJson(response);
        assertEquals(MISSING_PARAMETER_ERROR, json.get(ERROR_FIELD).asText());
        assertEquals(MISSING_SPORT_DESCRIPTION, json.get(DESCRIPTION_FIELD).asText());
    }

    @Test
    void givenCruiseNotFound_whenBookSport_thenReturn404WithCruiseNotFoundError() {
        String body = createSportRequestBody(ANY_SPORT_NAME);
        doThrow(new CruiseNotFoundException())
                .when(travelerServiceMock)
                .bookSport(ANY_CRUISE_ID, ANY_BOOKING_ID, ANY_TRAVELER_ID, ANY_SPORT_NAME);

        Response response = target(sportsPath())
                .request()
                .post(Entity.json(body));

        assertEquals(Response.Status.NOT_FOUND.getStatusCode(), response.getStatus());

        JsonNode json = readJson(response);
        assertEquals(CRUISE_NOT_FOUND_ERROR, json.get(ERROR_FIELD).asText());
        assertEquals(CRUISE_NOT_FOUND_DESCRIPTION, json.get(DESCRIPTION_FIELD).asText());
    }

    @Test
    void givenTravelerNotFound_whenBookSport_thenReturn404WithTravelerNotFoundError() {
        String body = createSportRequestBody(ANY_SPORT_NAME);
        doThrow(new TravelerNotFoundException())
                .when(travelerServiceMock)
                .bookSport(ANY_CRUISE_ID, ANY_BOOKING_ID, ANY_TRAVELER_ID, ANY_SPORT_NAME);

        Response response = target(sportsPath())
                .request()
                .post(Entity.json(body));

        assertEquals(Response.Status.NOT_FOUND.getStatusCode(), response.getStatus());

        JsonNode json = readJson(response);
        assertEquals(TRAVELER_NOT_FOUND_ERROR, json.get(ERROR_FIELD).asText());
        assertEquals(TRAVELER_NOT_FOUND_DESCRIPTION, json.get(DESCRIPTION_FIELD).asText());
    }

    @Test
    void givenSportNotFound_whenBookSport_thenReturn404WithSportNotFoundError() {
        String body = createSportRequestBody(ANY_SPORT_NAME);
        doThrow(new SportNotFoundException())
                .when(travelerServiceMock)
                .bookSport(ANY_CRUISE_ID, ANY_BOOKING_ID, ANY_TRAVELER_ID, ANY_SPORT_NAME);

        Response response = target(sportsPath())
                .request()
                .post(Entity.json(body));

        assertEquals(Response.Status.NOT_FOUND.getStatusCode(), response.getStatus());

        JsonNode json = readJson(response);
        assertEquals(SPORT_NOT_FOUND_ERROR, json.get(ERROR_FIELD).asText());
        assertEquals(SPORT_NOT_FOUND_DESCRIPTION, json.get(DESCRIPTION_FIELD).asText());
    }

    @Test
    void givenSportNotBookable_whenBookSport_thenReturn400WithSportNotBookableError() {
        String body = createSportRequestBody(ANY_SPORT_NAME);
        doThrow(new SportNotBookableException())
                .when(travelerServiceMock)
                .bookSport(ANY_CRUISE_ID, ANY_BOOKING_ID, ANY_TRAVELER_ID, ANY_SPORT_NAME);

        Response response = target(sportsPath())
                .request()
                .post(Entity.json(body));

        assertEquals(Response.Status.BAD_REQUEST.getStatusCode(), response.getStatus());

        JsonNode json = readJson(response);
        assertEquals(SPORT_NOT_BOOKABLE_ERROR, json.get(ERROR_FIELD).asText());
        assertEquals(SPORT_NOT_BOOKABLE_DESCRIPTION, json.get(DESCRIPTION_FIELD).asText());
    }

    @Test
    void givenSportAlreadyBooked_whenBookSport_thenReturn400WithSportAlreadyBookedError() {
        String body = createSportRequestBody(ANY_SPORT_NAME);
        doThrow(new SportAlreadyBookedException())
                .when(travelerServiceMock)
                .bookSport(ANY_CRUISE_ID, ANY_BOOKING_ID, ANY_TRAVELER_ID, ANY_SPORT_NAME);

        Response response = target(sportsPath())
                .request()
                .post(Entity.json(body));

        assertEquals(Response.Status.BAD_REQUEST.getStatusCode(), response.getStatus());

        JsonNode json = readJson(response);
        assertEquals(SPORT_ALREADY_BOOKED_ERROR, json.get(ERROR_FIELD).asText());
        assertEquals(SPORT_ALREADY_BOOKED_DESCRIPTION, json.get(DESCRIPTION_FIELD).asText());
    }
}
