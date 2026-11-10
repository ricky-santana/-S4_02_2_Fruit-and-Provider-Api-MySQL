package cat.itacademy.s04.t02.n02.fruit_api_MySQL.provider.dtos;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ProviderRequestDtoTest {

    private Validator validator;

    @BeforeEach
    void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    @DisplayName("ProviderRequestDto should be valid with proper fields")
    void shouldCreateProviderRequestDtoWithValidData() {
        ProviderRequestDto dto = new ProviderRequestDto("Frutas del Valle", "España");

        Set<ConstraintViolation<ProviderRequestDto>> violations = validator.validate(dto);

        assertTrue(violations.isEmpty());
    }

    @ParameterizedTest
    @NullAndEmptySource
    @ValueSource(strings = {" ", "   "})
    @DisplayName("ProviderRequestDto should fail validation when name is null or blank")
    void shouldFailValidationWhenNameIsBlankOrNull(String invalidName) {
        ProviderRequestDto dto = new ProviderRequestDto(invalidName, "España");

        Set<ConstraintViolation<ProviderRequestDto>> violations = validator.validate(dto);

        assertFalse(violations.isEmpty());
        assertTrue(violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("name")));
    }

    @ParameterizedTest
    @NullAndEmptySource
    @ValueSource(strings = {" ", "   "})
    @DisplayName("ProviderRequestDto should fail validation when country is null or blank")
    void shouldFailValidationWhenCountryIsBlankOrNull(String invalidCountry) {
        ProviderRequestDto dto = new ProviderRequestDto("Frutas del Valle", invalidCountry);

        Set<ConstraintViolation<ProviderRequestDto>> violations = validator.validate(dto);

        assertFalse(violations.isEmpty());
        assertTrue(violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("country")));
    }
}
