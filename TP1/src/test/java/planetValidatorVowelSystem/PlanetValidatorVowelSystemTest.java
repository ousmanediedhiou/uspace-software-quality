package planetValidatorVowelSystem;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class PlanetValidatorVowelSystemTest {

    @InjectMocks
    private PlanetValidatorVowelSystem validator;

    @Test
    void givenNull_whenValidate_thenFalse() {

        boolean result = validator.isPlanetValid(null);

        assertFalse(result);
    }

    @Test
    void givenEmptyString_whenValidate_thenFalse() {

        boolean result = validator.isPlanetValid("");

        assertFalse(result);
    }

    @Test
    void givenSpacesOnly_whenValidate_thenFalse() {

        boolean result = validator.isPlanetValid("   ");

        assertFalse(result);
    }

    @Test
    void givenNonAlphabetic_whenValidate_thenFalse() {

        boolean result = validator.isPlanetValid("Terre-9");

        assertFalse(result);
    }

    @Test
    void givenNoVowel_whenValidate_thenFalse() {

        boolean result = validator.isPlanetValid("PRSKQ");

        assertFalse(result);
    }

    @Test
    void givenOneVowel_whenValidate_thenTrue() {

        boolean result = validator.isPlanetValid("Zyxu");

        assertTrue(result);
    }

    @Test
    void givenSixVowelsTotal_whenValidate_thenFalse() {

        boolean result = validator.isPlanetValid("Aeioua");

        assertFalse(result);
    }

    @Test
    void givenFiveVowelsTotal_whenValidate_thenTrue() {

        boolean result = validator.isPlanetValid("ansoudiez");

        assertTrue(result);
    }

    @Test
    void givenThreeConsecutiveVowels_whenValidate_thenFalse() {

        boolean result = validator.isPlanetValid("Aaeer");

        assertFalse(result);
    }

    @Test
    void givenTwoConsecutiveVowels_whenValidate_thenTrue() {

        boolean result = validator.isPlanetValid("ansou");

        assertTrue(result);
    }

    @Test
    void givenInvalidExample_whenValidate_thenFalse() {

        boolean result = validator.isPlanetValid("SuperPlaneteInvalide");

        assertFalse(result);
    }

    @Test
    void givenSingleVowelName_whenValidate_thenTrue() {

        boolean result = validator.isPlanetValid("a");

        assertTrue(result);
    }

    @Test
    void givenMixedCaseName_whenValidate_thenTrue() {

        boolean result = validator.isPlanetValid("bA");

        assertTrue(result);
    }


    @Test
    void givenTerre_whenValidate_thenTrue() {

        boolean result = validator.isPlanetValid("Terre");

        assertTrue(result);
    }

    @Test
    void givenMars_whenValidate_thenTrue() {

        boolean result = validator.isPlanetValid("Mars");

        assertTrue(result);
    }

    @Test
    void givenJupiter_whenValidate_thenTrue() {

        boolean result = validator.isPlanetValid("Jupiter");

        assertTrue(result);
    }

    @Test
    void givenUranus_whenValidate_thenTrue() {

        boolean result = validator.isPlanetValid("Uranus");

        assertTrue(result);
    }

    @Test
    void givenSaturne_whenValidate_thenTrue() {

        boolean result = validator.isPlanetValid("Saturne");

        assertTrue(result);
    }

    @Test
    void givenNeptune_whenValidate_thenTrue() {

        boolean result = validator.isPlanetValid("Neptune");

        assertTrue(result);
    }

    @Test
    void givenMercure_whenValidate_thenTrue() {

        boolean result = validator.isPlanetValid("Mercure");

        assertTrue(result);
    }

    @Test
    void givenVenus_whenValidate_thenTrue() {

        boolean result = validator.isPlanetValid("Venus");

        assertTrue(result);
    }
}
