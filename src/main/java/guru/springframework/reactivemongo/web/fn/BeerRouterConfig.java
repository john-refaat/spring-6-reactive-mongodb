package guru.springframework.reactivemongo.web.fn;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.web.reactive.function.server.RequestPredicates.accept;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;

/**
 * @author john
 * @since 02/11/2024
 */
@RequiredArgsConstructor
@Configuration
public class BeerRouterConfig {

    private final BeerHandler beerHandler;
    
    public static final String BEER_PATH = "/api/v3/beer";
    public static final String BEER_PATH_ID = BEER_PATH + "/{beerId}";

    @Bean
    public RouterFunction<ServerResponse> beerRoutes() {
        return route()
                .GET(BEER_PATH, accept(APPLICATION_JSON),beerHandler::listBeers)
                .GET(BEER_PATH_ID, accept(APPLICATION_JSON),  beerHandler::getBeerById)
                .POST(BEER_PATH, accept(APPLICATION_JSON), beerHandler::saveBeer)
                .PUT(BEER_PATH_ID, accept(APPLICATION_JSON), beerHandler::updateBeer)
                .PATCH(BEER_PATH_ID, accept(APPLICATION_JSON), beerHandler::patchBeer)
                .DELETE(BEER_PATH_ID, accept(APPLICATION_JSON), beerHandler::deleteBeer)
                .build();
    }
}
