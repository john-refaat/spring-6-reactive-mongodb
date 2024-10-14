package guru.springframework.reactivemongo.service;

import guru.springframework.reactivemongo.mapper.BeerMapper;
import guru.springframework.reactivemongo.model.BeerDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;

import static org.awaitility.Awaitility.await;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

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
                .beerName("Test Beer")
                .beerStyle("Test Style")
                .price(BigDecimal.TEN)
                .upc("1234567890123")
                .build();
    }

    @Test
    void saveBeer() throws InterruptedException {
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
    void findById() {
    }
}