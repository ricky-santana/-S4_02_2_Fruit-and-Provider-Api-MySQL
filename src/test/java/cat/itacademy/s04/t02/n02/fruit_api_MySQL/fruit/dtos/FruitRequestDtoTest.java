package cat.itacademy.s04.t02.n02.fruit_api_MySQL.fruit.dtos;

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

class FruitRequestDtoTest {

    private Validator validator;

    @BeforeEach
    void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    @DisplayName("FruitRequestDto should pass validation with valid data")
    void shouldPassValidationWithValidData() {
        FruitRequestDto dto = new FruitRequestDto("Manzana", 10, 1L);

        Set<ConstraintViolation<FruitRequestDto>> violations = validator.validate(dto);

        assertTrue(violations.isEmpty());
    }

    @ParameterizedTest
    @NullAndEmptySource
    @ValueSource(strings = {" ", "   "})
    @DisplayName("FruitRequestDto should fail validation when name is null or blank")
    void shouldFailValidationWhenNameIsBlankOrNull(String invalidName) {
        FruitRequestDto dto = new FruitRequestDto(invalidName, 10, 1L);

        Set<ConstraintViolation<FruitRequestDto>> violations = validator.validate(dto);

        assertFalse(violations.isEmpty());
        assertTrue(violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("name")));
    }

    @Test
    @DisplayName("FruitRequestDto should fail validation when weight is null")
    void shouldFailValidationWhenWeightIsNull() {
        FruitRequestDto dto = new FruitRequestDto("Manzana", null, 1L);

        Set<ConstraintViolation<FruitRequestDto>> violations = validator.validate(dto);

        assertFalse(violations.isEmpty());
        assertTrue(violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("weightInKilos")));
    }

    @ParameterizedTest
    @ValueSource(ints = {0, -1, -50})
    @DisplayName("FruitRequestDto should fail validation when weight is zero or negative")
    void shouldFailValidationWhenWeightIsZeroOrNegative(int invalidWeight) {
        FruitRequestDto dto = new FruitRequestDto("Manzana", invalidWeight, 1L);

        Set<ConstraintViolation<FruitRequestDto>> violations = validator.validate(dto);

        assertFalse(violations.isEmpty());
        assertTrue(violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("weightInKilos")));
    }

    @Test
    @DisplayName("FruitRequestDto should fail validation when providerId is null")
    void shouldFailValidationWhenProviderIdIsNull() {
        FruitRequestDto dto = new FruitRequestDto("Manzana", 10, null);

        Set<ConstraintViolation<FruitRequestDto>> violations = validator.validate(dto);

        assertFalse(violations.isEmpty());
        assertTrue(violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("providerId")));
    }

    @ParameterizedTest
    @ValueSource(longs = {0L, -1L, -100L})
    @DisplayName("FruitRequestDto should fail validation when providerId is zero or negative")
    void shouldFailValidationWhenProviderIdIsZeroOrNegative(long invalidProviderId) {
        FruitRequestDto dto = new FruitRequestDto("Manzana", 10, invalidProviderId);

        Set<ConstraintViolation<FruitRequestDto>> violations = validator.validate(dto);

        assertFalse(violations.isEmpty());
        assertTrue(violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("providerId")));
    }
}
