package cat.itacademy.s04.t02.n02.fruit_api_MySQL.provider.dtos;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ProviderResponseDtoTest {

    @Test
    @DisplayName("ProviderResponseDto should store and expose all fields correctly")
    void shouldCreateProviderResponseDtoWithCorrectFields() {
        ProviderResponseDto responseDto = new ProviderResponseDto(1L, "Frutas del Valle", "España");

        assertEquals(1L, responseDto.id());
        assertEquals("Frutas del Valle", responseDto.name());
        assertEquals("España", responseDto.country());
    }
}
