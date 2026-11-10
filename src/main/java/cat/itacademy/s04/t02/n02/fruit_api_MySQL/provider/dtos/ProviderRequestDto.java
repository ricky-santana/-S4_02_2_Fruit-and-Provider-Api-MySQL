package cat.itacademy.s04.t02.n02.fruit_api_MySQL.provider.dtos;

import jakarta.validation.constraints.NotBlank;

public record ProviderRequestDto(
        @NotBlank
        String name,
        @NotBlank
        String country) {}
