package uz.in.currency.domain.exception;

public class CurrencyNotSaveException extends RuntimeException {
    public CurrencyNotSaveException(String message) {
        super(message);
    }
}
