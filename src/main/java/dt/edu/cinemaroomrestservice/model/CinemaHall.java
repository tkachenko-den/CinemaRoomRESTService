package dt.edu.cinemaroomrestservice.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * Объект, представляющий собой кинозал. Содержит следующие поля данных:
 * <br> totalRows - количество рядов в зале
 * <br> totalColumns - количество мест в ряду
 * <br> priceFormula - формула расчёта стоимости места
 * <br> allPlaces - все места зала
 * <br> soldPlaces - проданные места
 */
public class CinemaHall {
    int totalRows;
    int totalColumns;
    Function<Integer, Integer> priceFormula;

    private Map<PlacePosition, HallPlace> allPlaces;
    private Map<String, HallPlace> soldPlaces;

    /**
     * Метод возвращает массив доступных (не проданных) мест в зале
     * @return Массив мест в зале, отсортированный по ряду/месту
     */
    @JsonProperty("available_seats")
    public List<HallPlace> availablePlaces() {
        return allPlaces.values().stream()
                .filter(HallPlace::isAvailable)
                .sorted(HallPlace.comparator())
                .collect(Collectors.toUnmodifiableList());
    }

    @JsonProperty("total_rows")
    public int getTotalRows() {
        return totalRows;
    }

    @JsonProperty("total_columns")
    public int getTotalColumns() {
        return totalColumns;
    }

    /**
     * Метод инициализации мест в кинозале. Вызывается при создании зала.
     */
    private void initPlaces() {
        allPlaces = new ConcurrentHashMap<>(totalRows * totalColumns);
        for (int ii = 1; ii <= totalRows; ii++) {
            for (int jj = 1; jj <= totalColumns; jj++) {
                var position = new PlacePosition(ii, jj);
                allPlaces.put(position, new HallPlace(position, priceFormula.apply(ii), true));
            }
        }
    }

    /**
     * Операция покупки места в зале
     *
     * @param position - позиция места в зале
     * @return в случае успешной покупки (место есть в зале и не продано) возвращается объект купленного места.
     * @throws PlaceSoldException     - в случае если место продано
     * @throws PlaceNotFoundException - в случае если место не найдено в зале
     */
    public HallPlace purchasePlace(PlacePosition position) throws PlaceSoldException, PlaceNotFoundException {
        var place = allPlaces.get(position);
        if (place != null) {
            place.purchase();
            soldPlaces.put(place.getToken(), place);
        } else throw new PlaceNotFoundException("The number of a row or a column is out of bounds!");
        return place;
    }

    /**
     * Возврат купленного билета
     * @param token уникальный токен, сопоставленный с билетом
     * @return в случае успеха возвращается информация о возвращённом бидете
     * @throws WrongTokenException токен не найден в проданных билетах
     * @throws PlaceNotSoldException токен найден, но место не помечено как проданное
     */
    public HallPlace returnPlace(String token) throws WrongTokenException, PlaceNotSoldException {
        var place = soldPlaces.get(token);
        if (place != null) {
            place.returnTicket();
            soldPlaces.remove(token);
        } else throw new WrongTokenException("Wrong token!");
        return place;
    }

    /**
     * Метод возвращает суммарную стоимость проданных билетов
     * @return сумма проданных билетов
     */
    public int currentIncome() {
        return soldPlaces.values().stream()
                .mapToInt(HallPlace::getPrice)
                .sum();
    }

    /**
     * Метод возвращает кол-во свободных мест в зале
     * @return кол-во свободных мест в зале
     */
    public int availableSeatsNumber() {
        return availablePlaces().size();
    }

    /**
     * Метод возвращает кол-во проданных билетов
     * @return кол-во проданных билетов
     */
    public int purchasedTicketsNumber() {
        return soldPlaces.size();
    }

    /**
     * Конструкт объекта "Зал кинотеатра". Создаёт новый объект зада с указанным количеством мест
     * и помечает все места как свободные
     * @param totalRows кол-во рядов в зале
     * @param totalColumns количество мест в каждом ряде.
     */
    public CinemaHall(int totalRows, int totalColumns, Function<Integer, Integer> priceFormula) {
        this.totalRows = totalRows;
        this.totalColumns = totalColumns;
        this.priceFormula = priceFormula;
        initPlaces();
        soldPlaces = new ConcurrentHashMap<>(totalRows * totalColumns);
    }

    @Override
    public String toString() {
        return "total Columns: " + this.totalColumns + ", total Rows: " + this.totalRows;
    }
}