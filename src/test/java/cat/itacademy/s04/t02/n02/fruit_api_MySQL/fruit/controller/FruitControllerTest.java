package cat.itacademy.s04.t02.n02.fruit_api_MySQL.fruit.controller;

import cat.itacademy.s04.t02.n02.fruit_api_MySQL.common.exceptions.FruitNotFound;
import cat.itacademy.s04.t02.n02.fruit_api_MySQL.common.exceptions.ProviderNotFound;
import cat.itacademy.s04.t02.n02.fruit_api_MySQL.fruit.dtos.FruitRequestDto;
import cat.itacademy.s04.t02.n02.fruit_api_MySQL.fruit.dtos.FruitResponseDto;
import cat.itacademy.s04.t02.n02.fruit_api_MySQL.fruit.service.FruitService;
import cat.itacademy.s04.t02.n02.fruit_api_MySQL.provider.dtos.ProviderResponseDto;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import tools.jackson.databind.ObjectMapper;

import java.util.Collections;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(FruitController.class)
class FruitControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private FruitService fruitService;

    @Test
    @DisplayName("POST /fruits - Should register fruit and return 201 Created")
    void shouldRegisterFruitSuccessfully() throws Exception {
        FruitRequestDto requestDto = new FruitRequestDto("Manzana", 10, 1L);
        ProviderResponseDto providerDto = new ProviderResponseDto(1L, "Frutas del Valle", "España");
        FruitResponseDto responseDto = new FruitResponseDto(1L, "Manzana", 10, providerDto);

        when(fruitService.registerFruit(any(FruitRequestDto.class))).thenReturn(responseDto);

        mockMvc.perform(post("/fruits")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.name").value("Manzana"))
                .andExpect(jsonPath("$.weightInKilos").value(10))
                .andExpect(jsonPath("$.provider.id").value(1L))
                .andExpect(jsonPath("$.provider.name").value("Frutas del Valle"));
    }

    @Test
    @DisplayName("POST /fruits - Should return 404 Not Found when provider does not exist")
    void shouldReturnNotFoundWhenRegisteringFruitWithNonExistentProvider() throws Exception {
        FruitRequestDto requestDto = new FruitRequestDto("Manzana", 10, 99L);

        when(fruitService.registerFruit(any(FruitRequestDto.class)))
                .thenThrow(new ProviderNotFound("Can't register this fruit."));

        mockMvc.perform(post("/fruits")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("Provider not found. Can't register this fruit."));
    }

    @Test
    @DisplayName("POST /fruits - Should return 400 Bad Request when name is blank")
    void shouldReturnBadRequestWhenRegisteringFruitWithBlankName() throws Exception {
        FruitRequestDto invalidDto = new FruitRequestDto("", 10, 1L);

        mockMvc.perform(post("/fruits")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidDto)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").exists());
    }

    @Test
    @DisplayName("POST /fruits - Should return 400 Bad Request when weight is not positive")
    void shouldReturnBadRequestWhenRegisteringFruitWithNonPositiveWeight() throws Exception {
        FruitRequestDto invalidDto = new FruitRequestDto("Manzana", 0, 1L);

        mockMvc.perform(post("/fruits")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidDto)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").exists());
    }

    @Test
    @DisplayName("POST /fruits - Should return 400 Bad Request when providerId is null")
    void shouldReturnBadRequestWhenRegisteringFruitWithNullProviderId() throws Exception {
        FruitRequestDto invalidDto = new FruitRequestDto("Manzana", 10, null);

        mockMvc.perform(post("/fruits")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidDto)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").exists());
    }

    @Test
    @DisplayName("GET /fruits - Should return all fruits and 200 OK when no filter is provided")
    void shouldReturnAllFruitsWhenNoFilterProvided() throws Exception {
        ProviderResponseDto providerDto = new ProviderResponseDto(1L, "Frutas del Valle", "España");
        FruitResponseDto fruit1 = new FruitResponseDto(1L, "Manzana", 10, providerDto);
        FruitResponseDto fruit2 = new FruitResponseDto(2L, "Pera", 5, providerDto);

        when(fruitService.getAllFruits()).thenReturn(List.of(fruit1, fruit2));

        mockMvc.perform(get("/fruits"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(2))
                .andExpect(jsonPath("$[0].id").value(1L))
                .andExpect(jsonPath("$[0].name").value("Manzana"))
                .andExpect(jsonPath("$[1].id").value(2L))
                .andExpect(jsonPath("$[1].name").value("Pera"));
    }

    @Test
    @DisplayName("GET /fruits?providerId=1 - Should return fruits filtered by provider ID and 200 OK")
    void shouldReturnFruitsFilteredByProviderId() throws Exception {
        ProviderResponseDto providerDto = new ProviderResponseDto(1L, "Frutas del Valle", "España");
        FruitResponseDto fruit = new FruitResponseDto(1L, "Manzana", 10, providerDto);

        when(fruitService.getFruitsByProviderId(1L)).thenReturn(List.of(fruit));

        mockMvc.perform(get("/fruits").param("providerId", "1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(1))
                .andExpect(jsonPath("$[0].id").value(1L))
                .andExpect(jsonPath("$[0].name").value("Manzana"))
                .andExpect(jsonPath("$[0].provider.id").value(1L));
    }

    @Test
    @DisplayName("GET /fruits?providerId=99 - Should return 404 Not Found when provider does not exist")
    void shouldReturnNotFoundWhenFilteringByNonExistentProviderId() throws Exception {
        when(fruitService.getFruitsByProviderId(99L))
                .thenThrow(new ProviderNotFound("No provider with this ID."));

        mockMvc.perform(get("/fruits").param("providerId", "99"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("Provider not found. No provider with this ID."));
    }

    @Test
    @DisplayName("GET /fruits - Should return empty list and 200 OK when no fruits exist")
    void shouldReturnEmptyListWhenNoFruitsExist() throws Exception {
        when(fruitService.getAllFruits()).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/fruits"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(0));
    }

    @Test
    @DisplayName("GET /fruits/{id} - Should return fruit by id and 200 OK")
    void shouldReturnFruitById() throws Exception {
        ProviderResponseDto providerDto = new ProviderResponseDto(1L, "Frutas del Valle", "España");
        FruitResponseDto fruit = new FruitResponseDto(1L, "Manzana", 10, providerDto);

        when(fruitService.getFruitById(1L)).thenReturn(fruit);

        mockMvc.perform(get("/fruits/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.name").value("Manzana"))
                .andExpect(jsonPath("$.weightInKilos").value(10))
                .andExpect(jsonPath("$.provider.name").value("Frutas del Valle"));
    }

    @Test
    @DisplayName("GET /fruits/{id} - Should return 404 Not Found when fruit does not exist")
    void shouldReturnNotFoundWhenFruitDoesNotExist() throws Exception {
        when(fruitService.getFruitById(99L))
                .thenThrow(new FruitNotFound("No fruit with this ID."));

        mockMvc.perform(get("/fruits/99"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("Fruit not found. No fruit with this ID."));
    }

    @Test
    @DisplayName("PUT /fruits/{id} - Should update fruit and return 200 OK")
    void shouldUpdateFruitSuccessfully() throws Exception {
        FruitRequestDto updateDto = new FruitRequestDto("Manzana Fuji", 15, 2L);
        ProviderResponseDto newProvider = new ProviderResponseDto(2L, "Nuevo Proveedor", "Portugal");
        FruitResponseDto updatedResponse = new FruitResponseDto(1L, "Manzana Fuji", 15, newProvider);

        when(fruitService.updateFruit(eq(1L), any(FruitRequestDto.class))).thenReturn(updatedResponse);

        mockMvc.perform(put("/fruits/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.name").value("Manzana Fuji"))
                .andExpect(jsonPath("$.weightInKilos").value(15))
                .andExpect(jsonPath("$.provider.id").value(2L))
                .andExpect(jsonPath("$.provider.name").value("Nuevo Proveedor"));
    }

    @Test
    @DisplayName("PUT /fruits/{id} - Should return 404 Not Found when updating non-existent fruit")
    void shouldReturnNotFoundWhenUpdatingNonExistentFruit() throws Exception {
        FruitRequestDto updateDto = new FruitRequestDto("Manzana Fuji", 15, 1L);

        when(fruitService.updateFruit(eq(99L), any(FruitRequestDto.class)))
                .thenThrow(new FruitNotFound("No fruit with this ID."));

        mockMvc.perform(put("/fruits/99")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateDto)))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("Fruit not found. No fruit with this ID."));
    }

    @Test
    @DisplayName("PUT /fruits/{id} - Should return 404 Not Found when updating with non-existent provider")
    void shouldReturnNotFoundWhenUpdatingFruitWithNonExistentProvider() throws Exception {
        FruitRequestDto updateDto = new FruitRequestDto("Manzana Fuji", 15, 99L);

        when(fruitService.updateFruit(eq(1L), any(FruitRequestDto.class)))
                .thenThrow(new ProviderNotFound("Can't update fruit."));

        mockMvc.perform(put("/fruits/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateDto)))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("Provider not found. Can't update fruit."));
    }

    @Test
    @DisplayName("PUT /fruits/{id} - Should return 400 Bad Request when update body is invalid")
    void shouldReturnBadRequestWhenUpdatingFruitWithInvalidData() throws Exception {
        FruitRequestDto invalidDto = new FruitRequestDto("", -1, null);

        mockMvc.perform(put("/fruits/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidDto)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").exists());
    }

    @Test
    @DisplayName("DELETE /fruits/{id} - Should delete fruit and return 204 No Content")
    void shouldDeleteFruitSuccessfully() throws Exception {
        doNothing().when(fruitService).deleteFruit(1L);

        mockMvc.perform(delete("/fruits/1"))
                .andExpect(status().isNoContent());
    }

    @Test
    @DisplayName("DELETE /fruits/{id} - Should return 404 Not Found when deleting non-existent fruit")
    void shouldReturnNotFoundWhenDeletingNonExistentFruit() throws Exception {
        doThrow(new FruitNotFound("No fruit with this ID.")).when(fruitService).deleteFruit(99L);

        mockMvc.perform(delete("/fruits/99"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("Fruit not found. No fruit with this ID."));
    }
}
