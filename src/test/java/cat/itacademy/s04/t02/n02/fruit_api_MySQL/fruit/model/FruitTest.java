package cat.itacademy.s04.t02.n02.fruit_api_MySQL.fruit.model;

import cat.itacademy.s04.t02.n02.fruit_api_MySQL.provider.model.Provider;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class FruitTest {

    @Test
    @DisplayName("Should create fruit with name, weight, provider and null id initially")
    void shouldCreateFruitSuccessfully() {
        Provider provider = new Provider("Frutas Paco", "España");
        Fruit fruit = new Fruit("Manzana", 10, provider);

        assertNull(fruit.getId());
        assertEquals("Manzana", fruit.getName());
        assertEquals(10, fruit.getWeightInKilos());
        assertEquals(provider, fruit.getProvider());
    }

    @Test
    @DisplayName("Should update fruit fields and associated provider")
    void shouldUpdateFruitSuccessfully() {
        Provider initialProvider = new Provider("Frutas Paco", "España");
        Provider newProvider = new Provider("Tropical Export", "Costa Rica");
        Fruit fruit = new Fruit("Manzana", 10, initialProvider);

        fruit.update("Pera", 20, newProvider);

        assertEquals("Pera", fruit.getName());
        assertEquals(20, fruit.getWeightInKilos());
        assertEquals(newProvider, fruit.getProvider());
    }
}
