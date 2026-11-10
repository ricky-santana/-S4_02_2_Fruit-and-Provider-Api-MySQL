package cat.itacademy.s04.t02.n02.fruit_api_MySQL.provider.service;

import cat.itacademy.s04.t02.n02.fruit_api_MySQL.common.exceptions.ProviderAlreadyExists;
import cat.itacademy.s04.t02.n02.fruit_api_MySQL.common.exceptions.ProviderNotFound;
import cat.itacademy.s04.t02.n02.fruit_api_MySQL.provider.dtos.ProviderRequestDto;
import cat.itacademy.s04.t02.n02.fruit_api_MySQL.provider.dtos.ProviderResponseDto;
import cat.itacademy.s04.t02.n02.fruit_api_MySQL.provider.model.Provider;
import cat.itacademy.s04.t02.n02.fruit_api_MySQL.provider.repository.IProviderRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProviderServiceTest {

    @Mock
    private IProviderRepository providerRepository;

    @InjectMocks
    private ProviderService providerService;

    @Test
    @DisplayName("Should register a provider when name does not exist")
    void shouldRegisterProviderSuccessfully() {
        ProviderRequestDto requestDto = new ProviderRequestDto("Frutas del Valle", "España");
        Provider savedProvider = new Provider("Frutas del Valle", "España");
        ReflectionTestUtils.setField(savedProvider, "id", 1L);

        when(providerRepository.existsByName("Frutas del Valle")).thenReturn(false);
        when(providerRepository.save(any(Provider.class))).thenReturn(savedProvider);

        ProviderResponseDto result = providerService.registerProvider(requestDto);

        assertNotNull(result);
        assertEquals(1L, result.id());
        assertEquals("Frutas del Valle", result.name());
        assertEquals("España", result.country());
        verify(providerRepository, times(1)).existsByName("Frutas del Valle");
        verify(providerRepository, times(1)).save(any(Provider.class));
    }

    @Test
    @DisplayName("Should throw ProviderAlreadyExists when registering duplicate name")
    void shouldThrowExceptionWhenRegisteringDuplicateName() {
        ProviderRequestDto requestDto = new ProviderRequestDto("Frutas del Valle", "España");

        when(providerRepository.existsByName("Frutas del Valle")).thenReturn(true);

        assertThrows(ProviderAlreadyExists.class, () -> providerService.registerProvider(requestDto));
        verify(providerRepository, never()).save(any(Provider.class));
    }

    @Test
    @DisplayName("Should return a list of all providers")
    void shouldReturnAllProviders() {
        Provider provider1 = new Provider("Frutas del Valle", "España");
        ReflectionTestUtils.setField(provider1, "id", 1L);
        Provider provider2 = new Provider("Tropical Export", "Costa Rica");
        ReflectionTestUtils.setField(provider2, "id", 2L);

        when(providerRepository.findAll()).thenReturn(List.of(provider1, provider2));

        List<ProviderResponseDto> result = providerService.getAllProviders();

        assertEquals(2, result.size());
        assertEquals("Frutas del Valle", result.get(0).name());
        assertEquals("Tropical Export", result.get(1).name());
        verify(providerRepository, times(1)).findAll();
    }

    @Test
    @DisplayName("Should return an empty list when no providers exist")
    void shouldReturnEmptyListWhenNoProviders() {
        when(providerRepository.findAll()).thenReturn(Collections.emptyList());

        List<ProviderResponseDto> result = providerService.getAllProviders();

        assertTrue(result.isEmpty());
        verify(providerRepository, times(1)).findAll();
    }

    @Test
    @DisplayName("Should update provider when id exists and new name is available")
    void shouldUpdateProviderSuccessfully() {
        Provider existingProvider = new Provider("Frutas del Valle", "España");
        ReflectionTestUtils.setField(existingProvider, "id", 1L);
        ProviderRequestDto updateDto = new ProviderRequestDto("Frutas del Sur", "Portugal");

        when(providerRepository.findById(1L)).thenReturn(Optional.of(existingProvider));
        when(providerRepository.existsByName("Frutas del Sur")).thenReturn(false);
        when(providerRepository.save(existingProvider)).thenReturn(existingProvider);

        ProviderResponseDto result = providerService.updateProvider(1L, updateDto);

        assertEquals(1L, result.id());
        assertEquals("Frutas del Sur", result.name());
        assertEquals("Portugal", result.country());
        verify(providerRepository, times(1)).save(existingProvider);
    }

    @Test
    @DisplayName("Should update provider country when keeping the same name")
    void shouldUpdateProviderWhenKeepingSameName() {
        Provider existingProvider = new Provider("Frutas del Valle", "España");
        ReflectionTestUtils.setField(existingProvider, "id", 1L);
        ProviderRequestDto updateDto = new ProviderRequestDto("Frutas del Valle", "Portugal");

        when(providerRepository.findById(1L)).thenReturn(Optional.of(existingProvider));
        when(providerRepository.save(existingProvider)).thenReturn(existingProvider);

        ProviderResponseDto result = providerService.updateProvider(1L, updateDto);

        assertEquals("Frutas del Valle", result.name());
        assertEquals("Portugal", result.country());
        verify(providerRepository, never()).existsByName(anyString());
        verify(providerRepository, times(1)).save(existingProvider);
    }

    @Test
    @DisplayName("Should throw ProviderNotFound when updating non-existent provider")
    void shouldThrowProviderNotFoundWhenUpdatingNonExistentId() {
        ProviderRequestDto updateDto = new ProviderRequestDto("Frutas del Sur", "Portugal");
        when(providerRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(ProviderNotFound.class, () -> providerService.updateProvider(99L, updateDto));
        verify(providerRepository, never()).save(any(Provider.class));
    }

    @Test
    @DisplayName("Should throw ProviderAlreadyExists when updating to an already taken name")
    void shouldThrowProviderAlreadyExistsWhenUpdatingToExistingName() {
        Provider existingProvider = new Provider("Frutas del Valle", "España");
        ReflectionTestUtils.setField(existingProvider, "id", 1L);
        ProviderRequestDto updateDto = new ProviderRequestDto("Nombre Ocupado", "Portugal");

        when(providerRepository.findById(1L)).thenReturn(Optional.of(existingProvider));
        when(providerRepository.existsByName("Nombre Ocupado")).thenReturn(true);

        assertThrows(ProviderAlreadyExists.class, () -> providerService.updateProvider(1L, updateDto));
        verify(providerRepository, never()).save(any(Provider.class));
    }
}
