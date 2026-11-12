package cat.itacademy.s04.t02.n02.fruit_api_MySQL.config;

import cat.itacademy.s04.t02.n02.fruit_api_MySQL.fruit.model.Fruit;
import cat.itacademy.s04.t02.n02.fruit_api_MySQL.fruit.repository.IFruitRepository;
import cat.itacademy.s04.t02.n02.fruit_api_MySQL.provider.model.Provider;
import cat.itacademy.s04.t02.n02.fruit_api_MySQL.provider.repository.IProviderRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class DataBaseLoader {

    @Bean
    CommandLineRunner databaseInit(IFruitRepository fruitRepository, IProviderRepository providerRepository) {
        return args -> {
            if (providerRepository.count() == 0) {
                Provider provider1 = new Provider("Frutas del Valle", "España");
                Provider provider2 = new Provider("Tropical Export", "Costa Rica");

                providerRepository.saveAll(List.of(provider1, provider2));

                Fruit fruit1 = new Fruit("Manzana Fuji", 10, provider1);
                Fruit fruit2 = new Fruit("Plátano de Canarias", 15, provider1);
                Fruit fruit3 = new Fruit("Mango", 8, provider2);
                Fruit fruit4 = new Fruit("Papaya", 5, provider2);

                fruitRepository.saveAll(List.of(fruit1, fruit2, fruit3, fruit4));
            }
        };
    }
}
