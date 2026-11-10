package cat.itacademy.s04.t02.n02.fruit_api_MySQL.fruit.model;

import cat.itacademy.s04.t02.n02.fruit_api_MySQL.provider.model.Provider;
import jakarta.persistence.*;

@Entity
@Table(name = "fruits")
public class Fruit {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private int weightInKilos;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "provider_id", nullable = false)
    private Provider provider;

    public Fruit() {
    }

    public Fruit(String name, int weightInKilos, Provider provider) {
        this.name = name;
        this.weightInKilos = weightInKilos;
        this.provider = provider;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getWeightInKilos() {
        return weightInKilos;
    }

    public Provider getProvider() {
        return provider;
    }

    public void update(String name, int weightInKilos, Provider provider){
        this.name = name;
        this.weightInKilos = weightInKilos;
        this.provider = provider;
    }
}
