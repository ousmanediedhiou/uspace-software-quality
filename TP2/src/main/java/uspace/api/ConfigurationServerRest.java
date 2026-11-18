package uspace.api;

import jakarta.inject.Singleton;
import org.glassfish.hk2.utilities.binding.AbstractBinder;
import planetValidatorVowelSystem.PlanetValidatorVowelSystem;
import uspace.api.cruise.booking.NewBookingDtoValidator;
import uspace.api.cruise.booking.traveler.zeroGravityExperienceRequest.ZeroGravityExperienceRequestValidator;
import uspace.api.cruise.crew.CrewMemberDtoValidator;
import uspace.api.cruise.planet.PlanetDtoValidator;
import uspace.application.account.AccountAssembler;
import uspace.application.account.AccountService;
import uspace.application.cruise.crew.CrewMemberService;
import uspace.application.cruise.planet.PlanetAssembler;
import uspace.application.cruise.planet.PlanetService;
import uspace.application.cruise.planet.excursion.ExcursionAssembler;
import uspace.application.cruise.planet.excursion.ExcursionService;
import uspace.application.utils.dateTimeParser.LocalDateTimeParser;
import uspace.application.cruise.booking.BookingAssembler;
import uspace.application.cruise.booking.BookingService;
import uspace.application.cruise.CruiseAssembler;
import uspace.application.cruise.CruiseService;
import uspace.application.cruise.booking.BookingTravelerAssembler;
import uspace.application.cruise.booking.traveler.TravelerService;
import uspace.domain.account.AccountRepository;
import uspace.domain.cruise.CruiseRepository;
import uspace.domain.cruise.booking.BookingCostCalculator;
import uspace.domain.cruise.booking.BookingFactory;
import uspace.domain.cruise.booking.CostCalculator;
import uspace.domain.cruise.booking.traveler.TravelerFactory;
import uspace.domain.cruise.itinerary.planet.PlanetValidator;
import uspace.infra.persistence.hibernate.HibernateAccountRepository;
import uspace.api.account.NewAccountDtoValidator;
import uspace.infra.persistence.inMemory.InMemoryAccountRepository;

import java.time.format.DateTimeFormatter;

public class ConfigurationServerRest extends AbstractBinder {

    @Override
    protected void configure() {
        bindAsContract(TravelerFactory.class);
        bindAsContract(BookingFactory.class);

        bindAsContract(PlanetAssembler.class);
        bindAsContract(BookingTravelerAssembler.class);
        bindAsContract(BookingAssembler.class);
        bindAsContract(CruiseAssembler.class);

        bind(DateTimeFormatter.ISO_LOCAL_DATE_TIME).to(DateTimeFormatter.class);
        bindAsContract(LocalDateTimeParser.class);

        bind(PlanetValidatorVowelSystem.class).to(PlanetValidator.class);

        //bind(HibernateCruiseRepository.class).to(CruiseRepository.class).in(Singleton.class);
        bind(uspace.infra.persistence.hibernate.HibernateCruiseRepository.class).to(uspace.domain.cruise.CruiseRepository.class).in(jakarta.inject.Singleton.class);
        //bind(uspace.infra.persistence.inMemory.InMemoryCruiseRepository.class).to(uspace.domain.cruise.CruiseRepository.class).in(jakarta.inject.Singleton.class);

        bindAsContract(NewAccountDtoValidator.class);
        bindAsContract(AccountAssembler.class);
        bindAsContract(AccountService.class);
        bind(InMemoryAccountRepository.class).to(AccountRepository.class).in(Singleton.class);

        bind(HibernateAccountRepository.class).to(AccountRepository.class).in(Singleton.class);
        bindAsContract(AccountService.class);
        bindAsContract(AccountAssembler.class);


        bindAsContract(CrewMemberDtoValidator.class);
        bindAsContract(PlanetDtoValidator.class);
        bindAsContract(ZeroGravityExperienceRequestValidator.class);
        bindAsContract(NewBookingDtoValidator.class);

        bindAsContract(CrewMemberService.class);
        bindAsContract(uspace.application.cruise.crew.StandardCrewCabinPolicy.class)
                .to(uspace.application.cruise.crew.CabinAllocationPolicy.class);
        bindAsContract(PlanetService.class);
        bindAsContract(TravelerService.class);
        bindAsContract(BookingService.class);
        bindAsContract(CruiseService.class);

        bind(ExcursionService.class).to(ExcursionService.class);
        bind(ExcursionAssembler.class).to(ExcursionAssembler.class);
        bind(BookingCostCalculator.class).to(CostCalculator.class).in(Singleton.class);
        bind(uspace.infra.pricing.InMemoryCruisePricingProvider.class)
                .to(uspace.domain.cruise.booking.pricing.CruisePricingProvider.class)
                .in(Singleton.class);
    }
}
