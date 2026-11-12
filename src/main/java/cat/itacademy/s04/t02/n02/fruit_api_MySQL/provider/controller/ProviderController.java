package cat.itacademy.s04.t02.n02.fruit_api_MySQL.provider.controller;

import cat.itacademy.s04.t02.n02.fruit_api_MySQL.provider.dtos.ProviderRequestDto;
import cat.itacademy.s04.t02.n02.fruit_api_MySQL.provider.dtos.ProviderResponseDto;
import cat.itacademy.s04.t02.n02.fruit_api_MySQL.provider.service.ProviderService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class ProviderController {
    private final ProviderService providerService;

    public ProviderController(ProviderService providerService) {
        this.providerService = providerService;
    }

    @PostMapping("/providers")
    public ResponseEntity<ProviderResponseDto> registerProvider(@Valid @RequestBody ProviderRequestDto providerRequestDto){
        ProviderResponseDto createdProvider = providerService.registerProvider(providerRequestDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdProvider);
    }

    @GetMapping("/providers")
    public ResponseEntity<List<ProviderResponseDto>> getAllProviders(){
        List<ProviderResponseDto> providerList = providerService.getAllProviders();
        return ResponseEntity.ok(providerList);
    }

    @GetMapping("/providers/{id}")
    public ResponseEntity<ProviderResponseDto> getProviderById(@PathVariable Long id){
        ProviderResponseDto foundedProvider = providerService.getProviderById(id);
        return ResponseEntity.ok(foundedProvider);
    }

    @PutMapping("/providers/{id}")
    public ResponseEntity<ProviderResponseDto> updateProvider(@PathVariable Long id, @Valid @RequestBody ProviderRequestDto providerNewValues){
        ProviderResponseDto updatedProvider = providerService.updateProvider(id, providerNewValues);
        return ResponseEntity.ok(updatedProvider);
    }

    @DeleteMapping("/providers/{id}")
    public ResponseEntity<Void> deleteProviderById(@PathVariable Long id){
        providerService.deleteProvider(id);
        return ResponseEntity.noContent().build();
    }
}
