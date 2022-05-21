package dt.edu.cinemaroomrestservice.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import java.util.Comparator;
import java.util.UUID;

/**
 * Место зала кинотеатра. Содержит поля:
 * position - позиция (ряд и место) этого места в зале,
 * price - стоимость билета на это место,
 * isAvailable - свободно ли (не продано ли) место
 */
@JsonPropertyOrder({"row", "column", "price"})
public class HallPlace {
    PlacePosition position;
    volatile int price;
    volatile boolean isAvailable;
    volatile String token;

    @Override
    public String toString() {
        return "HallPlace{" +
                "position=" + position +
                ", price=" + price +
                ", isAvailable=" + isAvailable +
                '}';
    }

    public static Comparator<HallPlace> comparator() {
        return (p1, p2) -> (p1.getRow() * 100 + p1.getColumn()) - (p2.getRow() * 100 + p2.getColumn());
    }

    public HallPlace(PlacePosition position, int price, boolean isAvailable) {
        this.position = position;
        this.price = price;
        this.isAvailable = isAvailable;
        this.token = UUID.randomUUID().toString();
    }

    @JsonIgnore
    public PlacePosition getPosition() {
        return position;
    }

    public int getRow() {
        return position.getRow();
    }

    public int getColumn() {
        return position.getColumn();
    }

    public int getPrice() {
        return price;
    }

    synchronized public void setPrice(int price) {
        this.price = price;
    }

    @JsonIgnore
    public boolean isAvailable() {
        return isAvailable;
    }

    @JsonIgnore
    public String getToken() {
        return token;
    }

    synchronized public void purchase() throws PlaceSoldException {
        if (!isAvailable())
            throw new PlaceSoldException("The ticket has been already purchased!");
        isAvailable = false;
    }

    synchronized public void returnTicket() throws PlaceNotSoldException {
        if (isAvailable())
            throw new PlaceNotSoldException("The ticket doesn't sell! Internal error!");
        isAvailable = true;
        token = UUID.randomUUID().toString();
    }

}
