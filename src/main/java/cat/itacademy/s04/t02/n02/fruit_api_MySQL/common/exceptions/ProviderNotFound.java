package cat.itacademy.s04.t02.n02.fruit_api_MySQL.common.exceptions;

public class ProviderNotFound extends RuntimeException {
    public ProviderNotFound(String message) {
        super("Provider not found. " + message);
    }
}
