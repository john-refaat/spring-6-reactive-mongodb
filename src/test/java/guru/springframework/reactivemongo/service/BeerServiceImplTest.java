package guru.springframework.reactivemongo.service;

import guru.springframework.reactivemongo.mapper.BeerMapper;
import guru.springframework.reactivemongo.model.BeerDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;

import static org.awaitility.Awaitility.await;
import static org.junit.jupiter.api.Assertions.*;

/**
 * @author john
 * @since 13/10/2024
 */
@SpringBootTest
class BeerServiceImplTest {

    @Autowired
    BeerService beerService;

    @Autowired
    BeerMapper beerMapper;

    BeerDTO beerDTO;

    @BeforeEach
    void setUp() {
        beerDTO = BeerDTO.builder()
                .beerName("Stella")
                .beerStyle("Wheat")
                .price(BigDecimal.TEN)
                .upc("1234567890123")
                .build();
    }

    @Rollback
    @Test
    void saveBeer() {
        AtomicBoolean atomicBoolean = new AtomicBoolean(false);
        AtomicReference<BeerDTO> atomicReference = new AtomicReference<>();
        beerService.saveBeer(Mono.just(beerDTO)).subscribe(savedBeerDTO -> {
            atomicReference.set(savedBeerDTO);
            atomicBoolean.set(true);
        });

        await().untilTrue(atomicBoolean);
        assertNotNull(atomicReference.get());
        BeerDTO savedBeerDTO = atomicReference.get();
        assertNotNull(savedBeerDTO);
        System.out.println(savedBeerDTO);

        assertNotNull(savedBeerDTO.getId());
        assertEquals(beerDTO.getBeerName(), savedBeerDTO.getBeerName());
        assertEquals(beerDTO.getBeerStyle(), savedBeerDTO.getBeerStyle());
        assertEquals(beerDTO.getUpc(), savedBeerDTO.getUpc());
        assertEquals(beerDTO.getQuantityOnHand(), savedBeerDTO.getQuantityOnHand());
        assertEquals(beerDTO.getPrice(), savedBeerDTO.getPrice());
    }

    @Test
    void saveBeerWithBlock() {
        BeerDTO savedBeerDTO = beerService.saveBeer(Mono.just(beerDTO)).block();
        assertNotNull(savedBeerDTO);
        System.out.println(savedBeerDTO);

        assertNotNull(savedBeerDTO.getId());
        assertEquals(beerDTO.getBeerName(), savedBeerDTO.getBeerName());
        assertEquals(beerDTO.getBeerStyle(), savedBeerDTO.getBeerStyle());
        assertEquals(beerDTO.getUpc(), savedBeerDTO.getUpc());
        assertEquals(beerDTO.getQuantityOnHand(), savedBeerDTO.getQuantityOnHand());
        assertEquals(beerDTO.getPrice(), savedBeerDTO.getPrice());
    }

    @Test
    void updateBeer() {
        final String newBeerName = "New Stella";
        AtomicReference<BeerDTO> atomicReference = new AtomicReference<>();
        beerService.saveBeer(Mono.just(beerDTO))
                .map(savedBeer -> {
                    savedBeer.setBeerName(newBeerName);
                    return savedBeer;
                })
                // Update Beer
                .flatMap(beerService::saveBeer)
                .flatMap(updatedBeer-> beerService.findById(updatedBeer.getId()))
                .subscribe(atomicReference::set);

        await().until(() -> atomicReference.get()!= null);
        BeerDTO updatedBeerDTO = atomicReference.get();
        assertNotNull(updatedBeerDTO.getId());
        assertEquals(newBeerName, updatedBeerDTO.getBeerName());
    }

    @Test
    void updateBeerBlocking() {
        final String newBeerName = "New Stella";
        BeerDTO savedBeerDTO = getSavedBeerDTO();
        savedBeerDTO.setBeerName(newBeerName);
        savedBeerDTO.setPrice(BigDecimal.TWO);

        BeerDTO updatedBeerDTO = beerService.saveBeer(Mono.just(savedBeerDTO)).block();
        assertNotNull(updatedBeerDTO);
        assertNotNull(updatedBeerDTO.getId());
        assertEquals(newBeerName, updatedBeerDTO.getBeerName());

        BeerDTO fetchedBeerDTO = beerService.findById(savedBeerDTO.getId()).block();
        assertNotNull(fetchedBeerDTO);
        assertEquals(newBeerName, fetchedBeerDTO.getBeerName());
    }

    @Test
    void findById() {
        AtomicReference<BeerDTO> atomicReference = new AtomicReference<>();
        beerService.saveBeer(beerDTO)
                .map(BeerDTO::getId)
                .flatMap(beerService::findById)
                .subscribe(atomicReference::set);
        await().until(() -> atomicReference.get()!= null);
        BeerDTO foundBeerDTO = atomicReference.get();
        assertNotNull(foundBeerDTO);
        assertEquals(beerDTO.getBeerName(), foundBeerDTO.getBeerName());
        assertEquals(beerDTO.getBeerStyle(), foundBeerDTO.getBeerStyle());
        assertEquals(beerDTO.getUpc(), foundBeerDTO.getUpc());
        assertEquals(beerDTO.getQuantityOnHand(), foundBeerDTO.getQuantityOnHand());
    }

    @Test
    void deleteBeer() {
        BeerDTO savedBeerDTO = getSavedBeerDTO();
        assertNotNull(savedBeerDTO);
        beerService.deleteBeer(savedBeerDTO.getId()).block();
        assertNull(beerService.findById(savedBeerDTO.getId()).block());
    }

    BeerDTO getSavedBeerDTO() {
        return beerService.saveBeer(Mono.just(beerDTO)).block();
    }


}