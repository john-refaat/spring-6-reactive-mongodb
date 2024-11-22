package guru.springframework.reactivemongo.web.fn;

import guru.springframework.reactivemongo.mapper.BeerMapper;
import guru.springframework.reactivemongo.model.BeerDTO;
import guru.springframework.reactivemongo.model.PatchBeerDTO;
import guru.springframework.reactivemongo.repository.BeerRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.reactive.server.EntityExchangeResult;
import org.springframework.test.web.reactive.server.WebTestClient;

import java.math.BigDecimal;
import java.util.UUID;

import static org.springframework.security.test.web.reactive.server.SecurityMockServerConfigurers.mockJwt;

/**
 * @author john
 * @since 02/11/2024
 */
@SpringBootTest
@AutoConfigureWebTestClient
public class BeerEndpointTest {

    public static final String FOO_STYLE = "foo style";
    @Autowired
    WebTestClient client;

    @Autowired
    BeerRepository beerRepository;

    @Autowired
    BeerMapper beerMapper;


    private BeerDTO beerDTO;

    @BeforeEach
    void setUp() {
        client = client.mutateWith(mockJwt());
        beerDTO = BeerDTO.builder()
                .beerName("New Beer")
                .beerStyle("New Style")
                .upc("1234567890")
                .quantityOnHand(100)
                .price(BigDecimal.TEN)
                .build();
    }

    @Test
    void testListBeers() {
        client.get().uri("/api/v3/beer")
                .exchange()
                .expectStatus().isOk()
                .expectHeader().valueEquals("Content-type", "application/json")
                .expectBodyList(BeerDTO.class);
    }

    @Test
    void testSearchByBeerStyle() {
        beerDTO.setBeerStyle(FOO_STYLE);
        BeerDTO savedBeer = saveBeer();

        client.get().uri("/api/v3/beer?style={beerStyle}", FOO_STYLE)
               .exchange()
               .expectStatus().isOk()
               .expectHeader().valueEquals("Content-type", "application/json")
               .expectBodyList(BeerDTO.class).hasSize(1);
    }

    @Test
    void testGetBeerById() {
        BeerDTO savedBeer = saveBeer();
        EntityExchangeResult<BeerDTO> beerDTOEntityExchangeResult = client.get()
                .uri("/api/v3/beer/{beerId}", savedBeer.getId())
                .exchange()
                .expectStatus().isOk()
                .expectHeader().valueEquals("Content-type", "application/json")
                .expectBody(BeerDTO.class).returnResult();
        System.out.println(beerDTOEntityExchangeResult.getResponseBody());
    }

    @Test
    void testGetBeerByIdNotFound() {
        client.get()
               .uri("/api/v3/beer/{beerId}", UUID.randomUUID().toString())
               .exchange()
               .expectStatus().isNotFound();
    }

    @Test
    void testSaveBeer() {

        client.post().uri("/api/v3/beer")
               .bodyValue(beerDTO)
               .exchange()
               .expectStatus().isCreated()
               .expectHeader().exists("Location");
    }

    @Test
    void testSaveBeerBadRequest() {
        BeerDTO invalidBeer = BeerDTO.builder().beerName("").build();
        client.post().uri("/api/v3/beer")
               .bodyValue(invalidBeer)
               .exchange()
               .expectStatus().isBadRequest();
    }

    @Test
    void testUpdateBeer() {
        BeerDTO savedBeer = saveBeer();
        savedBeer.setBeerName("Updated Beer");

        client.put().uri("/api/v3/beer/{beerId}", savedBeer.getId())
               .bodyValue(savedBeer)
               .exchange()
               .expectStatus().isOk()
               .expectBody()
                .jsonPath("$.id").isEqualTo(savedBeer.getId())
                .jsonPath("$.beerName").isEqualTo(savedBeer.getBeerName());
    }

    @Test
    void testUpdateBeerBadRequest() {
        BeerDTO savedBeer = saveBeer();
        savedBeer.setBeerStyle("X");
        client.put().uri("/api/v3/beer/{beerId}", savedBeer.getId())
                .bodyValue(savedBeer)
               .exchange()
                .expectStatus().isBadRequest();

    }

    @Test
    void testPatchBeer() {
        BeerDTO savedBeer = saveBeer();
        //savedBeer.setBeerName("Patched Beer");
       PatchBeerDTO patched = PatchBeerDTO.builder().beerName("Patched Beer").build();

        client.patch().uri("/api/v3/beer/{beerId}", savedBeer.getId())
               .bodyValue(patched)
               .exchange()
               .expectStatus()
                .isNoContent();
                /*.isOk()
               .expectBody()
                .jsonPath("$.id").isEqualTo(savedBeer.getId())
                .jsonPath("$.beerName").isEqualTo(patched.getBeerName());*/
        client.get().uri("/api/v3/beer/{beerId}", savedBeer.getId())
                .exchange()
                .expectStatus().isOk()
                .expectBody().jsonPath("$.id").isEqualTo(savedBeer.getId())
                .jsonPath("$.beerName").isEqualTo(patched.getBeerName())
                .jsonPath("$.beerStyle").isEqualTo(savedBeer.getBeerStyle());
    }

    @Test
    void testPatchBeerNotFound() {
        client.patch().uri("/api/v3/beer/{beerId}", UUID.randomUUID().toString())
               .bodyValue(BeerDTO.builder().beerName("Patched Beer").build())
               .exchange()
               .expectStatus().isNotFound();
    }

    @Test
    void testPatchBeerBadRequest() {
        BeerDTO savedBeer = saveBeer();
        savedBeer.setBeerStyle("X");
        client.patch().uri("/api/v3/beer/{beerId}", savedBeer.getId())
               .bodyValue(savedBeer)
               .exchange()
               .expectStatus().isBadRequest();
    }

    @Test
    void testDeleteBeer() {
        BeerDTO savedBeer = saveBeer();
        client.delete().uri("/api/v3/beer/{beerId}", savedBeer.getId())
                .exchange()
                .expectStatus().isNoContent();

       /* client.get().uri("/api/v3/beer/{beerId}", savedBeer.getId())
               .exchange()
               .expectStatus().isNotFound();*/
    }

    @Test
    void testDeleteBeerNotFound() {
        client.delete().uri("/api/v3/beer/{beerId}", UUID.randomUUID().toString())
               .exchange()
               .expectStatus().isNotFound();
    }

    private BeerDTO saveBeer() {
        return beerRepository.save(beerMapper.beerDTOToBeer(beerDTO)).map(beerMapper::beerToBeerDTO).block();
    }



}
