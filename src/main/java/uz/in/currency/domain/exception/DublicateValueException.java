package uz.in.currency.domain.exception;

public class DublicateValueException extends RuntimeException{
    public DublicateValueException(String message) {
        super(message);
    }
}
