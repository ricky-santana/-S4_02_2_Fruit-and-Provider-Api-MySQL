package cat.itacademy.s04.t02.n02.fruit_api_MySQL.fruit.repository;

import cat.itacademy.s04.t02.n02.fruit_api_MySQL.fruit.model.Fruit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IFruitRepository extends JpaRepository<Fruit, Long> {

    List<Fruit> findByProviderId(Long providerId);
    boolean existsByProviderId(Long providerId);
}
