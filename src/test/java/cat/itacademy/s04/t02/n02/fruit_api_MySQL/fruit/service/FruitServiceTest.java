package cat.itacademy.s04.t02.n02.fruit_api_MySQL.fruit.service;

import cat.itacademy.s04.t02.n02.fruit_api_MySQL.common.exceptions.FruitNotFound;
import cat.itacademy.s04.t02.n02.fruit_api_MySQL.common.exceptions.ProviderNotFound;
import cat.itacademy.s04.t02.n02.fruit_api_MySQL.fruit.dtos.FruitRequestDto;
import cat.itacademy.s04.t02.n02.fruit_api_MySQL.fruit.dtos.FruitResponseDto;
import cat.itacademy.s04.t02.n02.fruit_api_MySQL.fruit.model.Fruit;
import cat.itacademy.s04.t02.n02.fruit_api_MySQL.fruit.repository.IFruitRepository;
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
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class FruitServiceTest {

    @Mock
    private IFruitRepository fruitRepository;

    @Mock
    private IProviderRepository providerRepository;

    @InjectMocks
    private FruitService fruitService;

    @Test
    @DisplayName("Should register a fruit introducing the correct fields")
    public void shouldRegisterFruit() {
        Provider provider = new Provider("Platanitos", "España");
        ReflectionTestUtils.setField(provider, "id", 1L);

        Fruit fruit = new Fruit("Platano de canarias", 1, provider);
        ReflectionTestUtils.setField(fruit, "id", 1L);

        FruitRequestDto expectedFruitRequestDto = new FruitRequestDto(fruit.getName(), fruit.getWeightInKilos(), provider.getId());
        ProviderResponseDto expectedProviderDto = new ProviderResponseDto(1L, "Platanitos", "España");

        when(providerRepository.findById(1L)).thenReturn(Optional.of(provider));
        when(fruitRepository.save(any(Fruit.class))).thenReturn(fruit);

        FruitResponseDto result = fruitService.registerFruit(expectedFruitRequestDto);

        assertEquals(fruit.getId(), result.id());
        assertEquals(fruit.getName(), result.name());
        assertEquals(fruit.getWeightInKilos(), result.weightInKilos());
        assertEquals(expectedProviderDto, result.provider());

        verify(providerRepository, times(1)).findById(1L);
        verify(fruitRepository, times(1)).save(any(Fruit.class));
    }

    @Test
    @DisplayName("Should throw ProviderNotFound when registering fruit with non-existent provider")
    public void shouldThrowProviderNotFoundWhenRegisteringFruitWithNonExistentProvider() {
        FruitRequestDto requestDto = new FruitRequestDto("Platano de canarias", 1, 99L);

        when(providerRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(ProviderNotFound.class, () -> fruitService.registerFruit(requestDto));

        verify(providerRepository, times(1)).findById(99L);
        verify(fruitRepository, never()).save(any(Fruit.class));
    }

    @Test
    @DisplayName("Should return all fruits when fruits exist")
    public void shouldReturnAllFruitsWhenFruitsExist() {
        Provider provider = new Provider("Frutas del Valle", "España");
        ReflectionTestUtils.setField(provider, "id", 1L);

        Fruit fruit1 = new Fruit("Manzana", 10, provider);
        ReflectionTestUtils.setField(fruit1, "id", 1L);
        Fruit fruit2 = new Fruit("Pera", 5, provider);
        ReflectionTestUtils.setField(fruit2, "id", 2L);

        when(fruitRepository.findAll()).thenReturn(List.of(fruit1, fruit2));

        List<FruitResponseDto> result = fruitService.getAllFruits();

        assertEquals(2, result.size());
        assertEquals(fruit1.getName(), result.get(0).name());
        assertEquals(fruit2.getName(), result.get(1).name());

        verify(fruitRepository, times(1)).findAll();
    }

    @Test
    @DisplayName("Should return empty list when no fruits exist")
    public void shouldReturnEmptyListWhenNoFruitsExist() {
        when(fruitRepository.findAll()).thenReturn(Collections.emptyList());

        List<FruitResponseDto> result = fruitService.getAllFruits();

        assertTrue(result.isEmpty());
        verify(fruitRepository, times(1)).findAll();
    }

    @Test
    @DisplayName("Should return fruits by provider ID when provider exists")
    public void shouldReturnFruitsByProviderIdWhenProviderExists() {
        Provider provider = new Provider("Tropical Export", "Costa Rica");
        ReflectionTestUtils.setField(provider, "id", 1L);

        Fruit fruit = new Fruit("Mango", 8, provider);
        ReflectionTestUtils.setField(fruit, "id", 1L);

        when(providerRepository.existsById(1L)).thenReturn(true);
        when(fruitRepository.findByProviderId(1L)).thenReturn(List.of(fruit));

        List<FruitResponseDto> result = fruitService.getFruitsByProviderId(1L);

        assertEquals(1, result.size());
        assertEquals(fruit.getName(), result.get(0).name());
        assertEquals(provider.getName(), result.get(0).provider().name());

        verify(providerRepository, times(1)).existsById(1L);
        verify(fruitRepository, times(1)).findByProviderId(1L);
    }

    @Test
    @DisplayName("Should throw ProviderNotFound when getting fruits by non-existent provider ID")
    public void shouldThrowProviderNotFoundWhenGettingFruitsByNonExistentProviderId() {
        when(providerRepository.existsById(99L)).thenReturn(false);

        assertThrows(ProviderNotFound.class, () -> fruitService.getFruitsByProviderId(99L));

        verify(providerRepository, times(1)).existsById(99L);
        verify(fruitRepository, never()).findByProviderId(anyLong());
    }

    @Test
    @DisplayName("Should return fruit by ID when it exists")
    public void shouldReturnFruitByIdWhenExists() {
        Provider provider = new Provider("Frutas del Valle", "España");
        ReflectionTestUtils.setField(provider, "id", 1L);

        Fruit fruit = new Fruit("Manzana", 10, provider);
        ReflectionTestUtils.setField(fruit, "id", 1L);

        when(fruitRepository.findById(1L)).thenReturn(Optional.of(fruit));

        FruitResponseDto result = fruitService.getFruitById(1L);

        assertEquals(fruit.getId(), result.id());
        assertEquals(fruit.getName(), result.name());
        assertEquals(fruit.getWeightInKilos(), result.weightInKilos());
        assertEquals(provider.getId(), result.provider().id());

        verify(fruitRepository, times(1)).findById(1L);
    }

    @Test
    @DisplayName("Should throw FruitNotFound when fruit ID does not exist")
    public void shouldThrowFruitNotFoundWhenFruitIdDoesNotExist() {
        when(fruitRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(FruitNotFound.class, () -> fruitService.getFruitById(99L));

        verify(fruitRepository, times(1)).findById(99L);
    }

    @Test
    @DisplayName("Should update fruit when fruit and provider exist")
    public void shouldUpdateFruitSuccessfully() {
        Provider oldProvider = new Provider("Viejo Proveedor", "España");
        ReflectionTestUtils.setField(oldProvider, "id", 1L);

        Provider newProvider = new Provider("Nuevo Proveedor", "Portugal");
        ReflectionTestUtils.setField(newProvider, "id", 2L);

        Fruit fruit = new Fruit("Manzana", 10, oldProvider);
        ReflectionTestUtils.setField(fruit, "id", 1L);

        FruitRequestDto updateDto = new FruitRequestDto("Manzana Fuji", 15, 2L);

        when(fruitRepository.findById(1L)).thenReturn(Optional.of(fruit));
        when(providerRepository.findById(2L)).thenReturn(Optional.of(newProvider));
        when(fruitRepository.save(fruit)).thenReturn(fruit);

        FruitResponseDto result = fruitService.updateFruit(1L, updateDto);

        assertEquals(1L, result.id());
        assertEquals("Manzana Fuji", result.name());
        assertEquals(15, result.weightInKilos());
        assertEquals(2L, result.provider().id());
        assertEquals("Nuevo Proveedor", result.provider().name());

        verify(fruitRepository, times(1)).findById(1L);
        verify(providerRepository, times(1)).findById(2L);
        verify(fruitRepository, times(1)).save(fruit);
    }

    @Test
    @DisplayName("Should throw FruitNotFound when updating non-existent fruit")
    public void shouldThrowFruitNotFoundWhenUpdatingNonExistentFruit() {
        FruitRequestDto updateDto = new FruitRequestDto("Manzana", 10, 1L);

        when(fruitRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(FruitNotFound.class, () -> fruitService.updateFruit(99L, updateDto));

        verify(fruitRepository, times(1)).findById(99L);
        verify(providerRepository, never()).findById(anyLong());
        verify(fruitRepository, never()).save(any(Fruit.class));
    }

    @Test
    @DisplayName("Should throw ProviderNotFound when updating fruit with non-existent provider")
    public void shouldThrowProviderNotFoundWhenUpdatingFruitWithNonExistentProvider() {
        Provider provider = new Provider("Proveedor", "España");
        ReflectionTestUtils.setField(provider, "id", 1L);

        Fruit fruit = new Fruit("Manzana", 10, provider);
        ReflectionTestUtils.setField(fruit, "id", 1L);

        FruitRequestDto updateDto = new FruitRequestDto("Manzana", 10, 99L);

        when(fruitRepository.findById(1L)).thenReturn(Optional.of(fruit));
        when(providerRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(ProviderNotFound.class, () -> fruitService.updateFruit(1L, updateDto));

        verify(fruitRepository, times(1)).findById(1L);
        verify(providerRepository, times(1)).findById(99L);
        verify(fruitRepository, never()).save(any(Fruit.class));
    }

    @Test
    @DisplayName("Should delete fruit successfully when ID exists")
    public void shouldDeleteFruitSuccessfully() {
        Provider provider = new Provider("Proveedor", "España");
        ReflectionTestUtils.setField(provider, "id", 1L);

        Fruit fruit = new Fruit("Manzana", 10, provider);
        ReflectionTestUtils.setField(fruit, "id", 1L);

        when(fruitRepository.findById(1L)).thenReturn(Optional.of(fruit));

        fruitService.deleteFruit(1L);

        verify(fruitRepository, times(1)).findById(1L);
        verify(fruitRepository, times(1)).delete(fruit);
    }

    @Test
    @DisplayName("Should throw FruitNotFound when deleting non-existent fruit")
    public void shouldThrowFruitNotFoundWhenDeletingNonExistentFruit() {
        when(fruitRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(FruitNotFound.class, () -> fruitService.deleteFruit(99L));

        verify(fruitRepository, times(1)).findById(99L);
        verify(fruitRepository, never()).delete(any(Fruit.class));
    }
}
