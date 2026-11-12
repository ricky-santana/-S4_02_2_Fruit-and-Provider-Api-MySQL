package cat.itacademy.s04.t02.n02.fruit_api_MySQL.provider.controller;

import cat.itacademy.s04.t02.n02.fruit_api_MySQL.common.exceptions.ProviderAlreadyExists;
import cat.itacademy.s04.t02.n02.fruit_api_MySQL.common.exceptions.ProviderHasFruits;
import cat.itacademy.s04.t02.n02.fruit_api_MySQL.common.exceptions.ProviderNotFound;
import cat.itacademy.s04.t02.n02.fruit_api_MySQL.provider.dtos.ProviderRequestDto;
import cat.itacademy.s04.t02.n02.fruit_api_MySQL.provider.dtos.ProviderResponseDto;
import cat.itacademy.s04.t02.n02.fruit_api_MySQL.provider.service.ProviderService;
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

@WebMvcTest(ProviderController.class)
class ProviderControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private ProviderService providerService;

    @Test
    @DisplayName("POST /providers - Should register provider and return 201 Created")
    void shouldRegisterProviderSuccessfully() throws Exception {
        ProviderRequestDto requestDto = new ProviderRequestDto("Frutas del Valle", "España");
        ProviderResponseDto responseDto = new ProviderResponseDto(1L, "Frutas del Valle", "España");

        when(providerService.registerProvider(any(ProviderRequestDto.class))).thenReturn(responseDto);

        mockMvc.perform(post("/providers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.name").value("Frutas del Valle"))
                .andExpect(jsonPath("$.country").value("España"));
    }

    @Test
    @DisplayName("POST /providers - Should return 400 Bad Request when name is blank")
    void shouldReturnBadRequestWhenRegisteringProviderWithBlankName() throws Exception {
        ProviderRequestDto invalidDto = new ProviderRequestDto("", "España");

        mockMvc.perform(post("/providers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidDto)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").exists());
    }

    @Test
    @DisplayName("POST /providers - Should return 400 Bad Request when provider already exists")
    void shouldReturnBadRequestWhenRegisteringDuplicateProvider() throws Exception {
        ProviderRequestDto requestDto = new ProviderRequestDto("Frutas del Valle", "España");

        when(providerService.registerProvider(any(ProviderRequestDto.class)))
                .thenThrow(new ProviderAlreadyExists("Can't register provider."));

        mockMvc.perform(post("/providers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("It already exists a provider with this name. Can't register provider."));
    }

    @Test
    @DisplayName("GET /providers - Should return all providers and 200 OK")
    void shouldReturnAllProviders() throws Exception {
        ProviderResponseDto provider1 = new ProviderResponseDto(1L, "Frutas del Valle", "España");
        ProviderResponseDto provider2 = new ProviderResponseDto(2L, "Tropical Export", "Costa Rica");

        when(providerService.getAllProviders()).thenReturn(List.of(provider1, provider2));

        mockMvc.perform(get("/providers"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(2))
                .andExpect(jsonPath("$[0].id").value(1L))
                .andExpect(jsonPath("$[0].name").value("Frutas del Valle"))
                .andExpect(jsonPath("$[0].country").value("España"))
                .andExpect(jsonPath("$[1].id").value(2L))
                .andExpect(jsonPath("$[1].name").value("Tropical Export"))
                .andExpect(jsonPath("$[1].country").value("Costa Rica"));
    }

    @Test
    @DisplayName("GET /providers - Should return empty list and 200 OK when no providers exist")
    void shouldReturnEmptyListWhenNoProvidersExist() throws Exception {
        when(providerService.getAllProviders()).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/providers"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(0));
    }

    @Test
    @DisplayName("GET /providers/{id} - Should return provider by id and 200 OK")
    void shouldReturnProviderById() throws Exception {
        ProviderResponseDto responseDto = new ProviderResponseDto(1L, "Frutas del Valle", "España");

        when(providerService.getProviderById(1L)).thenReturn(responseDto);

        mockMvc.perform(get("/providers/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.name").value("Frutas del Valle"))
                .andExpect(jsonPath("$.country").value("España"));
    }

    @Test
    @DisplayName("GET /providers/{id} - Should return 404 Not Found when provider does not exist")
    void shouldReturnNotFoundWhenProviderDoesNotExist() throws Exception {
        when(providerService.getProviderById(99L)).thenThrow(new ProviderNotFound("With this id."));

        mockMvc.perform(get("/providers/99"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("Provider not found. With this id."));
    }

    @Test
    @DisplayName("PUT /providers/{id} - Should update provider and return 200 OK")
    void shouldUpdateProviderSuccessfully() throws Exception {
        ProviderRequestDto updateDto = new ProviderRequestDto("Frutas del Sur", "Portugal");
        ProviderResponseDto updatedResponse = new ProviderResponseDto(1L, "Frutas del Sur", "Portugal");

        when(providerService.updateProvider(eq(1L), any(ProviderRequestDto.class))).thenReturn(updatedResponse);

        mockMvc.perform(put("/providers/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.name").value("Frutas del Sur"))
                .andExpect(jsonPath("$.country").value("Portugal"));
    }

    @Test
    @DisplayName("PUT /providers/{id} - Should return 404 Not Found when updating non-existent provider")
    void shouldReturnNotFoundWhenUpdatingNonExistentProvider() throws Exception {
        ProviderRequestDto updateDto = new ProviderRequestDto("Frutas del Sur", "Portugal");

        when(providerService.updateProvider(eq(99L), any(ProviderRequestDto.class)))
                .thenThrow(new ProviderNotFound("With this id."));

        mockMvc.perform(put("/providers/99")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateDto)))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("Provider not found. With this id."));
    }

    @Test
    @DisplayName("PUT /providers/{id} - Should return 400 Bad Request when updating with existing name")
    void shouldReturnBadRequestWhenUpdatingWithExistingName() throws Exception {
        ProviderRequestDto updateDto = new ProviderRequestDto("Nombre Ocupado", "Portugal");

        when(providerService.updateProvider(eq(1L), any(ProviderRequestDto.class)))
                .thenThrow(new ProviderAlreadyExists("Can't update provider."));

        mockMvc.perform(put("/providers/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateDto)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("It already exists a provider with this name. Can't update provider."));
    }

    @Test
    @DisplayName("PUT /providers/{id} - Should return 400 Bad Request when request body is invalid")
    void shouldReturnBadRequestWhenUpdatingProviderWithInvalidData() throws Exception {
        ProviderRequestDto invalidDto = new ProviderRequestDto("", "");

        mockMvc.perform(put("/providers/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidDto)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").exists());
    }

    @Test
    @DisplayName("DELETE /providers/{id} - Should delete provider and return 204 No Content")
    void shouldDeleteProviderSuccessfully() throws Exception {
        doNothing().when(providerService).deleteProvider(1L);

        mockMvc.perform(delete("/providers/1"))
                .andExpect(status().isNoContent());
    }

    @Test
    @DisplayName("DELETE /providers/{id} - Should return 404 Not Found when deleting non-existent provider")
    void shouldReturnNotFoundWhenDeletingNonExistentProvider() throws Exception {
        doThrow(new ProviderNotFound("With this id.")).when(providerService).deleteProvider(99L);

        mockMvc.perform(delete("/providers/99"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("Provider not found. With this id."));
    }

    @Test
    @DisplayName("DELETE /providers/{id} - Should return 400 Bad Request when provider has fruits")
    void shouldReturnBadRequestWhenDeletingProviderWithFruits() throws Exception {
        doThrow(new ProviderHasFruits("Can't delete this provider.")).when(providerService).deleteProvider(1L);

        mockMvc.perform(delete("/providers/1"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("This provider has fruits. Can't delete this provider."));
    }
}
