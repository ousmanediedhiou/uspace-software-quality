package uspace.api;

import org.glassfish.hk2.utilities.binding.AbstractBinder;
import planetValidatorVowelSystem.PlanetValidatorVowelSystem;
import uspace.api.account.AccountDtoValidator;
import uspace.api.cruise.booking.NewBookingDtoValidator;
import uspace.api.cruise.booking.traveler.SportBookingDtoValidator;
import uspace.api.cruise.booking.traveler.zeroGravityExperienceRequest.ZeroGravityExperienceRequestValidator;
import uspace.api.cruise.crew.CrewMemberDtoValidator;
import uspace.api.cruise.planet.ExcursionDtoValidator;
import uspace.api.cruise.planet.NewPlanetDtoValidator;
import uspace.application.account.AccountAssembler;
import uspace.application.account.AccountService;
import uspace.application.cruise.crew.CrewMemberAssembler;
import uspace.application.cruise.crew.CrewMemberService;
import uspace.application.cruise.emergencyShuttles.*;
import uspace.application.cruise.planet.PlanetAssembler;
import uspace.application.cruise.planet.PlanetService;
import uspace.application.cruise.planet.excursion.ExcursionAssembler;
import uspace.application.utils.dateTimeParser.LocalDateTimeParser;
import uspace.application.cruise.booking.BookingAssembler;
import uspace.application.cruise.booking.BookingService;
import uspace.application.cruise.CruiseAssembler;
import uspace.application.cruise.CruiseService;
import uspace.application.cruise.booking.BookingTravelerAssembler;
import uspace.application.cruise.booking.traveler.TravelerService;
import uspace.domain.account.AccountDeletionValidator;
import uspace.domain.account.AccountRepository;
import uspace.domain.cruise.CruiseRepository;
import uspace.domain.cruise.booking.BookingFactory;
import uspace.domain.cruise.booking.BookingCreator;
import uspace.domain.cruise.booking.bookingCostCalculator.BookingCostCalculatorFactory;
import uspace.domain.cruise.booking.invoice.InvoiceFactory;
import uspace.domain.cruise.booking.traveler.TravelerFactory;
import uspace.domain.cruise.emergencyShuttles.ShuttlesConfiguration;
import uspace.domain.cruise.itinerary.planet.PlanetValidator;
import uspace.domain.cruise.sport.SportRegistry;
import uspace.infra.emergencyShuttles.ShuttlesConfigurationJsonReader;
import uspace.infra.persistence.hibernate.HibernateAccountRepository;
import uspace.infra.persistence.hibernate.HibernateCruiseRepository;
import uspace.infra.persistence.inMemory.InMemoryAccountRepository;
import uspace.infra.persistence.inMemory.InMemoryCruiseRepository;
import uspace.infra.sport.SportJsonReader;
import uspace.application.cruise.cabin.CabinAttributionAssembler;
import uspace.application.cruise.cabin.CabinAttributionEngine;
import uspace.application.cruise.cabin.CabinAttributionService;
import uspace.domain.cruise.cabin.CabinConfiguration;
import uspace.infra.cabin.CabinJsonReader;


import java.time.format.DateTimeFormatter;


public class ConfigurationServerRest extends AbstractBinder {
    private static final String SPORT_JSON_PATH = "src/main/resources/data/sports.json";
    private static CruiseRepository CruiseRepositoryInstance = new HibernateCruiseRepository();
    private static AccountRepository AccountRepositoryInstance = new HibernateAccountRepository();
    private static final String CABINS_JSON_PATH = "src/main/resources/data/cabins.json";
    private static final String SHUTTLES_CONFIGURATION_JSON_PATH = "src/main/resources/data/shuttlesConfiguration.json";


    public static void useInMemoryRepositories() {
        CruiseRepositoryInstance = new InMemoryCruiseRepository();
        AccountRepositoryInstance = new InMemoryAccountRepository();
    }

    @Override
    protected void configure() {
        bindAsContract(BookingCostCalculatorFactory.class);
        bindAsContract(InvoiceFactory.class);
        bindAsContract(TravelerFactory.class);
        bindAsContract(BookingFactory.class);

        bindAsContract(AccountDeletionValidator.class);
        bindAsContract(BookingCreator.class);

        bindAsContract(ExcursionAssembler.class);
        bindAsContract(PlanetAssembler.class);
        bindAsContract(CrewMemberAssembler.class);
        bindAsContract(BookingTravelerAssembler.class);
        bindAsContract(BookingAssembler.class);
        bindAsContract(CruiseAssembler.class);
        bindAsContract(AccountAssembler.class);
        bindAsContract(ShuttleTravelerAssembler.class);
        bindAsContract(ShuttleCrewMemberAssembler.class);
        bindAsContract(ShuttleAssembler.class);
        bindAsContract(EmergencyShuttlesAssembler.class);

        bind(DateTimeFormatter.ISO_LOCAL_DATE_TIME).to(DateTimeFormatter.class);
        bindAsContract(LocalDateTimeParser.class);

        bind(PlanetValidatorVowelSystem.class).to(PlanetValidator.class);

        bind(CruiseRepositoryInstance).to(CruiseRepository.class);
        bind(AccountRepositoryInstance).to(AccountRepository.class);

        bind(new SportJsonReader(SPORT_JSON_PATH)).to(SportRegistry.class);
        bind(new CabinJsonReader(CABINS_JSON_PATH)).to(CabinConfiguration.class);
        bind(new ShuttlesConfigurationJsonReader(SHUTTLES_CONFIGURATION_JSON_PATH)).to(ShuttlesConfiguration.class);


        bindAsContract(CrewMemberDtoValidator.class);
        bindAsContract(ExcursionDtoValidator.class);
        bindAsContract(NewPlanetDtoValidator.class);
        bindAsContract(ZeroGravityExperienceRequestValidator.class);
        bindAsContract(NewBookingDtoValidator.class);
        bindAsContract(AccountDtoValidator.class);
        bindAsContract(SportBookingDtoValidator.class);

        bindAsContract(CrewMemberService.class);
        bindAsContract(PlanetService.class);
        bindAsContract(TravelerService.class);
        bindAsContract(BookingService.class);
        bindAsContract(CruiseService.class);
        bindAsContract(AccountService.class);
        bindAsContract(CabinAttributionEngine.class);
        bindAsContract(CabinAttributionAssembler.class);
        bindAsContract(CabinAttributionService.class);
        bindAsContract(EmergencyShuttlesService.class);

    }
}
