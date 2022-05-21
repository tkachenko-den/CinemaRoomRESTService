package dt.edu.cinemaroomrestservice.controllers.rest;

import dt.edu.cinemaroomrestservice.model.*;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
public class SeatsController {
    @Autowired
    CinemaHall cinemaHall;

    /**
     * The method of obtaining free seats for ordering in the cinema hall.
     * @return description of the hall - the number of rows and seats in each and an array of free seats.
     */
    @GetMapping("/seats")
    public CinemaHall getSeats() {
        return cinemaHall;
    }

    @PostMapping("/purchase")
    @JsonPropertyOrder({"token", "ticket"})
    public Map<String, Object> postPurchase(@RequestBody PlacePosition position)
            throws PlaceNotFoundException, PlaceSoldException {
        var place = cinemaHall.purchasePlace(position);
        return Map.of(
                "token", place.getToken(),
                "ticket", place
        );
    }

    @PostMapping("/return")
    public Map<String, Object> postReturnTicket(@RequestBody Map<String, String> request)
            throws BadRequestException, WrongTokenException, PlaceNotSoldException {
        var token = request.get("token");
        if (token==null) throw new BadRequestException("Incorrect request. Don't find token param.");
        return Map.of(
                "returned_ticket", cinemaHall.returnPlace(token)
        );
    }

    @PostMapping("/stats")
    public Map<String,Object> postStats(@RequestParam(required=false,defaultValue = "") String password)
            throws AuthException {
        checkAuth(password);
        return Map.of(
                "current_income", cinemaHall.currentIncome(),
                "number_of_available_seats",cinemaHall.availableSeatsNumber(),
                "number_of_purchased_tickets",cinemaHall.purchasedTicketsNumber()
        );
    }

    public void checkAuth(String password) throws AuthException {
        if (! "super_secret".equals(password)) throw new AuthException("The password is wrong!");
    }

    @ExceptionHandler({PlaceSoldException.class, PlaceNotFoundException.class, WrongTokenException.class,
            BadRequestException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Map<String, String> handleException400(Exception e) {
        return Map.of(
                "error", e.getMessage()
        );
    }

    @ExceptionHandler(PlaceNotSoldException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public Map<String, String> handleException500(Exception e) {
        return Map.of(
                "error", e.getMessage()
        );
    }

    @ExceptionHandler(AuthException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public Map<String, String> handleException401(Exception e) {
        return Map.of(
                "error", e.getMessage()
        );
    }

}
