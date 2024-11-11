package guru.springframework.reactivemongo.service;

import guru.springframework.reactivemongo.model.BeerDTO;
import guru.springframework.reactivemongo.model.PatchBeerDTO;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * @author john
 * @since 13/10/2024
 */
public interface BeerService {
    Mono<BeerDTO> saveBeer(Mono<BeerDTO> beerDTO);
    Mono<BeerDTO> saveBeer(BeerDTO beerDTO);
    Mono<BeerDTO> findById(String id);
    Mono<BeerDTO> updateBeer(String id, BeerDTO beerDTO);
    Mono<BeerDTO> patchBeer(String id, PatchBeerDTO beerDTO);
    Mono<Void> deleteBeer(String id);
    Mono<BeerDTO> findFirstByName(String name);
    Flux<BeerDTO> findByBeerStyle(String style);

    Flux<BeerDTO> findAll();
}
