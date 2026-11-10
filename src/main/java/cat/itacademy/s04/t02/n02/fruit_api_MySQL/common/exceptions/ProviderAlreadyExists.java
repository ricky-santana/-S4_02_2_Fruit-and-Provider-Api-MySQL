package cat.itacademy.s04.t02.n02.fruit_api_MySQL.common.exceptions;

public class ProviderAlreadyExists extends RuntimeException {
    public ProviderAlreadyExists(String message) {
        super("It already exists a provider with this name. " + message);
    }
}
