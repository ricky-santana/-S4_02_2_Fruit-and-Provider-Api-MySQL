package cat.itacademy.s04.t02.n02.fruit_api_MySQL.common.exceptions;

public class FruitNotFound extends RuntimeException {
    public FruitNotFound(String message) {
        super("Fruit not found. " + message);
    }
}
