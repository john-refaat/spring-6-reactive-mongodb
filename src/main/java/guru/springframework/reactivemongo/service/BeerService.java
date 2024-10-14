package guru.springframework.reactivemongo.service;

import guru.springframework.reactivemongo.model.BeerDTO;
import reactor.core.publisher.Mono;

/**
 * @author john
 * @since 13/10/2024
 */
public interface BeerService {
    Mono<BeerDTO> saveBeer(Mono<BeerDTO> beerDTO);
    Mono<BeerDTO> findById(String id);
}
