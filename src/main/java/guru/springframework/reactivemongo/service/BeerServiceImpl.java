package guru.springframework.reactivemongo.service;

import guru.springframework.reactivemongo.mapper.BeerMapper;
import guru.springframework.reactivemongo.model.BeerDTO;
import guru.springframework.reactivemongo.repository.BeerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

/**
 * @author john
 * @since 13/10/2024
 */
@RequiredArgsConstructor
@Service
public class BeerServiceImpl implements BeerService {

    private final BeerRepository beerRepository;
    private final BeerMapper beerMapper;

    @Override
    public Mono<BeerDTO> saveBeer(Mono<BeerDTO> beerDTO) {
        return beerDTO.map(beerMapper::beerDTOToBeer)
                .flatMap(beerRepository::save)
                .map(beerMapper::beerToBeerDTO);
    }

    @Override
    public Mono<BeerDTO> findById(String id) {
        return null;
    }
}
