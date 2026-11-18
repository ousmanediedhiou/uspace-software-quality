package uspace.application.planet;

import jakarta.inject.Inject;
import planetValidatorVowelSystem.PlanetValidatorVowelSystem;

public class VowelSystemPlanetValidator implements PlanetValidator {

    private final PlanetValidatorVowelSystem impl;



    @Inject
    public VowelSystemPlanetValidator(PlanetValidatorVowelSystem impl) {
        this.impl = impl;
    }

    @Override
    public boolean isValid(String planetName) {
        return impl.isPlanetValid(planetName);
    }
}
