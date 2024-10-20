package guru.springframework.reactivemongo.service;

import guru.springframework.reactivemongo.domain.Beer;
import guru.springframework.reactivemongo.mapper.BeerMapper;
import guru.springframework.reactivemongo.model.BeerDTO;
import guru.springframework.reactivemongo.repository.BeerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
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
    public Mono<BeerDTO> saveBeer(BeerDTO beerDTO) {
        return beerRepository.save(beerMapper.beerDTOToBeer(beerDTO)).map(beerMapper::beerToBeerDTO);
    }

    @Override
    public Mono<BeerDTO> findById(String id) {
        return beerRepository.findById(id).map(beerMapper::beerToBeerDTO);
    }

    @Override
    public Mono<BeerDTO> updateBeer(String id, BeerDTO beerDTO) {
        return beerRepository.findById(id)
                .map(foundBeer -> {
                    foundBeer.setBeerName(beerDTO.getBeerName());
                    foundBeer.setBeerStyle(beerDTO.getBeerStyle());
                    foundBeer.setUpc(beerDTO.getUpc());
                    foundBeer.setQuantityOnHand(beerDTO.getQuantityOnHand());
                    foundBeer.setPrice(beerDTO.getPrice());
                    return foundBeer;
                }).flatMap(beerRepository::save)
                .map(beerMapper::beerToBeerDTO);
    }

    @Override
    public Mono<BeerDTO> patchBeer(String id, BeerDTO beerDTO) {
        return beerRepository.findById(id).map(
                foundBeer -> {
                    foundBeer.setBeerName(beerDTO.getBeerName()!= null? beerDTO.getBeerName() : foundBeer.getBeerName());
                    foundBeer.setBeerStyle(beerDTO.getBeerStyle()!= null? beerDTO.getBeerStyle() : foundBeer.getBeerStyle());
                    foundBeer.setUpc(beerDTO.getUpc()!= null? beerDTO.getUpc() : foundBeer.getUpc());
                    foundBeer.setQuantityOnHand(beerDTO.getQuantityOnHand()!= null? beerDTO.getQuantityOnHand() : foundBeer.getQuantityOnHand());
                    foundBeer.setPrice(beerDTO.getPrice()!= null? beerDTO.getPrice() : foundBeer.getPrice());
                    return foundBeer;
                }).flatMap(beerRepository::save)
                .map(beerMapper::beerToBeerDTO);
    }

    @Override
    public Mono<Void> deleteBeer(String id) {
        return beerRepository.findById(id).switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND)))
                .map(Beer::getId).flatMap(beerRepository::deleteById);
    }
}
