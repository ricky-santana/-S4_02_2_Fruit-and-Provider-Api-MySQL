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
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FruitService {
    private final IFruitRepository fruitRepository;
    private final IProviderRepository providerRepository;

    public FruitService(IFruitRepository fruitRepository, IProviderRepository providerRepository) {
        this.fruitRepository = fruitRepository;
        this.providerRepository = providerRepository;
    }

    public FruitResponseDto registerFruit(FruitRequestDto fruitRequestDto){
        Provider provider = providerRepository.findById(fruitRequestDto.providerId())
                .orElseThrow(() ->new ProviderNotFound("Can't register this fruit."));

        Fruit fruit = new Fruit(fruitRequestDto.name(), fruitRequestDto.weightInKilos(), provider);
        Fruit savedFruit = fruitRepository.save(fruit);

        return toResponseDto(savedFruit);
    }

    public List<FruitResponseDto> getAllFruits(){
        return fruitRepository.findAll().stream()
                .map(this::toResponseDto)
                .toList();
    }

    public List<FruitResponseDto> getFruitsByProviderId(Long providerId){
        if (!providerRepository.existsById(providerId)) {
            throw new ProviderNotFound("No provider with this ID.");
        }

        return fruitRepository.findByProviderId(providerId).stream()
                .map(this::toResponseDto)
                .toList();
    }

    public FruitResponseDto getFruitById(Long fruitId){
        Fruit foundedFruit = findOrThrow(fruitId);
        return toResponseDto(foundedFruit);
    }

    public FruitResponseDto updateFruit(Long fruitId, FruitRequestDto fruitNewValues){
        Fruit fruit = findOrThrow(fruitId);

        Provider provider = providerRepository.findById(fruitNewValues.providerId())
                .orElseThrow(()-> new ProviderNotFound("Can't update fruit."));

        fruit.update(fruitNewValues.name(), fruitNewValues.weightInKilos(), provider);
        Fruit updatedFruit = fruitRepository.save(fruit);

        return toResponseDto(updatedFruit);
    }

    public void deleteFruit(Long id){
        Fruit fruitToDelete = findOrThrow(id);
        fruitRepository.delete(fruitToDelete);
    }

    private Fruit findOrThrow(Long fruitId){
      return fruitRepository.findById(fruitId)
              .orElseThrow(()->new FruitNotFound("No fruit with this ID."));
    }

    private FruitResponseDto toResponseDto(Fruit fruit){
        Provider provider = fruit.getProvider();
        ProviderResponseDto providerResponseDto = new ProviderResponseDto(
                provider.getId(),
                provider.getName(),
                provider.getCountry());
    //TODO preguntar Sergi sobre la forma de construir objetos y mezclar formas en el mismo método
        return new FruitResponseDto(fruit.getId(), fruit.getName(), fruit.getWeightInKilos(), providerResponseDto);
    }
}
