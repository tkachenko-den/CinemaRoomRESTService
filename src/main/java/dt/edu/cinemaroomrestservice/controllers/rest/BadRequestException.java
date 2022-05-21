package dt.edu.cinemaroomrestservice.controllers.rest;

public class BadRequestException extends Exception {
    public BadRequestException(String message) {
        super(message);
    }
}
