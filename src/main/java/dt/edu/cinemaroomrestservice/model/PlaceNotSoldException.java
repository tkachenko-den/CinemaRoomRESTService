package dt.edu.cinemaroomrestservice.model;

public class PlaceNotSoldException extends Exception {
    public PlaceNotSoldException(String message) {
        super(message);
    }
}
