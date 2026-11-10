package cat.itacademy.s04.t02.n02.fruit_api_MySQL.provider.model;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

class ProviderTest {

    @Test
    @DisplayName("Provider should be created with name and country, id being null initially")
    void shouldCreateProvider() {
        Provider provider = new Provider("Frutas del Valle", "España");

        assertNull(provider.getId());
        assertEquals("Frutas del Valle", provider.getName());
        assertEquals("España", provider.getCountry());
    }

    @Test
    @DisplayName("Provider should update name and country correctly")
    void shouldUpdateProviderFields() {
        Provider provider = new Provider("Frutas del Valle", "España");

        provider.update("Frutas del Sur", "Portugal");

        assertEquals("Frutas del Sur", provider.getName());
        assertEquals("Portugal", provider.getCountry());
    }
}
