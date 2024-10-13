package guru.springframework.reactivemongo.service;

import guru.springframework.reactivemongo.model.BeerDTO;

/**
 * @author john
 * @since 13/10/2024
 */
public interface BeerService {
    BeerDTO saveBeer(BeerDTO beerDTO);
    BeerDTO findById(String id);
}
