package cat.itacademy.s04.t02.n02.fruit_api_MySQL.provider.repository;

import cat.itacademy.s04.t02.n02.fruit_api_MySQL.provider.model.Provider;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IProviderRepository extends JpaRepository<Provider, Long> {

    boolean existsByName(String name);
}
