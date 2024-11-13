package guru.springframework.reactivemongo.service;

import guru.springframework.reactivemongo.domain.Beer;
import guru.springframework.reactivemongo.mapper.BeerMapper;
import guru.springframework.reactivemongo.model.BeerDTO;
import guru.springframework.reactivemongo.model.PatchBeerDTO;
import guru.springframework.reactivemongo.repository.BeerRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * @author john
 * @since 13/10/2024
 */
@Slf4j
@RequiredArgsConstructor
@Service
public class BeerServiceImpl implements BeerService {

    private final BeerRepository beerRepository;
    private final BeerMapper beerMapper;

    @Override
    public Mono<BeerDTO> saveBeer(Mono<BeerDTO> beerDTO) {
            return beerDTO.map(beerDTO1 -> {
            log.info("Saving Beer from Mono {}",beerDTO1);
            return beerDTO1;
        }).map(beerMapper::beerDTOToBeer)
                .flatMap(beerRepository::save)
                .map(beerMapper::beerToBeerDTO);
    }

    @Override
    public Mono<BeerDTO> saveBeer(BeerDTO beerDTO) {
        log.info("Saving beer object {}", beerDTO);
        return beerRepository.save(beerMapper.beerDTOToBeer(beerDTO)).map(beerMapper::beerToBeerDTO);
    }

    @Override
    public Mono<BeerDTO> findById(String id) {
        log.info("Finding beer by id {}", id);
        return beerRepository.findById(id).map(beerMapper::beerToBeerDTO);
    }

    @Override
    public Mono<BeerDTO> updateBeer(String id, BeerDTO beerDTO) {
        log.info("Updating beer by id {} with {}", id, beerDTO);
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
    public Mono<BeerDTO> patchBeer(String id, PatchBeerDTO beerDTO) {
        log.info("Patching beer by id {} with {}", id, beerDTO);
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
        log.info("Deleting beer by id {}", id);
        return beerRepository.deleteById(id);
        //return beerRepository.findById(id).map(Beer::getId).flatMap(beerRepository::deleteById);
    }

    @Override
    public Mono<BeerDTO> findFirstByName(String name) {
        log.info("Finding beer by name {}", name);
        return beerRepository.findFirstByBeerName(name).map(beerMapper::beerToBeerDTO);
    }

    @Override
    public Flux<BeerDTO> findByBeerStyle(String style) {
        log.info("Finding beers by style {}", style);
        return beerRepository.findByBeerStyle(style).map(beerMapper::beerToBeerDTO);
    }

    @Override
    public Flux<BeerDTO> findAll() {
        log.info("Finding all beers");
        return beerRepository.findAll().map(beerMapper::beerToBeerDTO);
    }
}
