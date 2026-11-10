package cat.itacademy.s04.t02.n02.fruit_api_MySQL.fruit.dtos;

import cat.itacademy.s04.t02.n02.fruit_api_MySQL.provider.dtos.ProviderResponseDto;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class FruitResponseDtoTest {

    @Test
    @DisplayName("FruitResponseDto should store and expose all fields including nested provider")
    void shouldCreateFruitResponseDtoSuccessfully() {
        ProviderResponseDto providerDto = new ProviderResponseDto(1L, "Frutas Paco", "España");
        FruitResponseDto responseDto = new FruitResponseDto(10L, "Manzana", 5, providerDto);

        assertEquals(10L, responseDto.id());
        assertEquals("Manzana", responseDto.name());
        assertEquals(5, responseDto.weightInKilos());
        assertEquals(providerDto, responseDto.provider());
    }
}
