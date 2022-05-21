package dt.edu.cinemaroomrestservice;

import dt.edu.cinemaroomrestservice.model.CinemaHall;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.function.Function;

@Configuration
public class CinemaConfiguration {
    final static int totalRows = 9;
    final static int totalColumns = 9;
    final static Function<Integer, Integer> priceFormula = row -> row <= 4 ? 10 : 8;

    @Bean
    public CinemaHall getCinemaHall() {
        return new CinemaHall(totalRows, totalColumns, priceFormula);
    }


}
