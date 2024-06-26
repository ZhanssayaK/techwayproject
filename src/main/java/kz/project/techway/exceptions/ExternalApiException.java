package kz.project.techway.exceptions;

public class ExternalApiException extends RuntimeException{
    public ExternalApiException(String message, Throwable cause) {
        super(message, cause);
    }

    public ExternalApiException(String message) {
        super(message);
    }
}
