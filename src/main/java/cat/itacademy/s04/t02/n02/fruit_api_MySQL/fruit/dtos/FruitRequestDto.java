package cat.itacademy.s04.t02.n02.fruit_api_MySQL.fruit.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record FruitRequestDto (
    @NotBlank
    String name,

    @NotNull
    @Positive
    Integer weightInKilos,

    @NotNull
    @Positive
    Long providerId){}
