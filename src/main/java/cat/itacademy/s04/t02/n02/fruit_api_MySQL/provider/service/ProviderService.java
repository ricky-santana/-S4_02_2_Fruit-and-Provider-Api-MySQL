package cat.itacademy.s04.t02.n02.fruit_api_MySQL.provider.service;

import cat.itacademy.s04.t02.n02.fruit_api_MySQL.common.exceptions.ProviderAlreadyExists;
import cat.itacademy.s04.t02.n02.fruit_api_MySQL.common.exceptions.ProviderHasFruits;
import cat.itacademy.s04.t02.n02.fruit_api_MySQL.common.exceptions.ProviderNotFound;
import cat.itacademy.s04.t02.n02.fruit_api_MySQL.fruit.repository.IFruitRepository;
import cat.itacademy.s04.t02.n02.fruit_api_MySQL.provider.dtos.ProviderRequestDto;
import cat.itacademy.s04.t02.n02.fruit_api_MySQL.provider.dtos.ProviderResponseDto;
import cat.itacademy.s04.t02.n02.fruit_api_MySQL.provider.model.Provider;
import cat.itacademy.s04.t02.n02.fruit_api_MySQL.provider.repository.IProviderRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProviderService {
    private final IProviderRepository providerRepository;
    private final IFruitRepository fruitRepository;

    public ProviderService(IProviderRepository providerRepository, IFruitRepository fruitRepository) {
        this.providerRepository = providerRepository;
        this.fruitRepository = fruitRepository;
    }

    public ProviderResponseDto registerProvider(ProviderRequestDto requestDto){
        providerNameExists(requestDto.name(), "Can't register provider.");

        Provider providerToSave = new Provider(requestDto.name(), requestDto.country());
        Provider savedProvider = providerRepository.save(providerToSave);

        return toResponseDto(savedProvider);
    }

    public List<ProviderResponseDto> getAllProviders(){
        return providerRepository.findAll().stream()
                .map(this::toResponseDto)
                .toList();
    }

    public ProviderResponseDto updateProvider(Long id, ProviderRequestDto providerNewValues){
        Provider findedProvider = findeOrThrow(id);

        if(!findedProvider.getName().equalsIgnoreCase(providerNewValues.name())){
            providerNameExists(providerNewValues.name(), "Can't update provider.");
        }

        findedProvider.update(providerNewValues.name(), providerNewValues.country());
        providerRepository.save(findedProvider);

        return toResponseDto(findedProvider);
    }

    public void deleteProvider(Long id){
        Provider providerToDelete = findeOrThrow(id);
        hasNotFruitOrThrow(id, "Can't delete this provider.");
        providerRepository.delete(providerToDelete);
    }

    private ProviderResponseDto toResponseDto (Provider provider){
        return new ProviderResponseDto(provider.getId(), provider.getName(), provider.getCountry());
    }

    private Provider findeOrThrow(Long id){
        return providerRepository.findById(id)
                .orElseThrow(()->new ProviderNotFound("With this id."));
    }

    private void providerNameExists(String name, String message){
        if(providerRepository.existsByName(name)){
            throw new ProviderAlreadyExists(message);
        }
    }

    private void hasNotFruitOrThrow(Long id, String message){
        if(fruitRepository.existsByProviderId(id)){
            throw new ProviderHasFruits(message);
        }
    }
}
