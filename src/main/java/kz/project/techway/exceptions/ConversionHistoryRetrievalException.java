package kz.project.techway.exceptions;

public class ConversionHistoryRetrievalException extends RuntimeException{
    public ConversionHistoryRetrievalException(String message) {
        super(message);
    }

    public ConversionHistoryRetrievalException(String message, Throwable cause) {
        super(message, cause);
    }
}
