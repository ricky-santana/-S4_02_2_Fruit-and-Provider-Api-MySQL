package cat.itacademy.s04.t02.n02.fruit_api_MySQL.common.exceptions;

public class ProviderHasFruits extends RuntimeException {
    public ProviderHasFruits(String message) {
        super("This provider has fruits. " + message);
    }
}
