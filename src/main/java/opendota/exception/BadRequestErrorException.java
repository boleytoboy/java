package opendota.exception;

public class BadRequestErrorException extends RuntimeException {
    public BadRequestErrorException(String message) {
        super(message);
    }
}