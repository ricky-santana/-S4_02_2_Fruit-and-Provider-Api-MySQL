package cat.itacademy.s04.t02.n02.fruit_api_MySQL.fruit.controller;

import cat.itacademy.s04.t02.n02.fruit_api_MySQL.fruit.dtos.FruitRequestDto;
import cat.itacademy.s04.t02.n02.fruit_api_MySQL.fruit.dtos.FruitResponseDto;
import cat.itacademy.s04.t02.n02.fruit_api_MySQL.fruit.service.FruitService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class FruitController {
    private final FruitService fruitService;

    public FruitController(FruitService fruitService) {
        this.fruitService = fruitService;
    }

    @PostMapping("/fruits")
    public ResponseEntity<FruitResponseDto> registerFruit(@Valid @RequestBody FruitRequestDto fruitToRegister){
        FruitResponseDto savedFruit = fruitService.registerFruit(fruitToRegister);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedFruit);
    }

    @GetMapping("/fruits")
    public ResponseEntity<List<FruitResponseDto>> getAllFruits(@RequestParam(required = false) Long providerId){
        List<FruitResponseDto> fruitList = (providerId == null)? fruitService.getAllFruits(): fruitService.getFruitsByProviderId(providerId);
        return ResponseEntity.ok(fruitList);
    }

    @GetMapping("/fruits/{id}")
    public ResponseEntity<FruitResponseDto> getFruitById(@PathVariable Long id){
        FruitResponseDto foundedFruit = fruitService.getFruitById(id);
        return ResponseEntity.ok(foundedFruit);
    }

    @PutMapping("/fruits/{id}")
    public ResponseEntity<FruitResponseDto> updateFruit(@PathVariable Long id, @Valid @RequestBody FruitRequestDto fruitNewValues){
        FruitResponseDto updatedFruit = fruitService.updateFruit(id, fruitNewValues);
        return ResponseEntity.ok(updatedFruit);
    }

    @DeleteMapping("/fruits/{id}")
    public ResponseEntity<Void> deleteFruitById(@PathVariable Long id){
        fruitService.deleteFruit(id);
        return ResponseEntity.noContent().build();
    }
}
