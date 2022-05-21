package dt.edu.cinemaroomrestservice.model;

public class WrongTokenException extends Exception{
    public WrongTokenException(String message) {
        super(message);
    }
}
