package cat.itacademy.s04.t02.n02.fruit_api_MySQL.fruit.dtos;

import cat.itacademy.s04.t02.n02.fruit_api_MySQL.provider.dtos.ProviderResponseDto;

public record FruitResponseDto(
        Long id,
        String name,
        int weightInKilos,
        ProviderResponseDto provider) {}
