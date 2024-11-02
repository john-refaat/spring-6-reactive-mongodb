package guru.springframework.reactivemongo.bootstrap;

import guru.springframework.reactivemongo.domain.Beer;
import guru.springframework.reactivemongo.repository.BeerRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

/**
 * @author john
 * @since 20/10/2024
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class BootstrapData implements CommandLineRunner  {

    private final BeerRepository beerRepository;

    @Override
    public void run(String... args) throws Exception {
        beerRepository.deleteAll().doOnSuccess(foo -> {
            loadBeerData();
        }).subscribe();
    }

    private void loadBeerData() {
        log.info("Load Beer Data");
        beerRepository.count().filter(count -> count==0).subscribe(count -> {
            log.info("Loading beer data");
            Beer beer1 = Beer.builder()
                    .beerName("Galaxy Cat")
                    .beerStyle("PALE_ALE")
                    .upc("12356")
                    .price(BigDecimal.valueOf(12.5))
                    .quantityOnHand(122)
                    .build();

            Beer beer2 = Beer.builder()
                    .beerName("Crank")
                    .beerStyle("Wheat")
                    .upc("12356222")
                    .price(BigDecimal.valueOf(11.80))
                    .quantityOnHand(392)
                    .build();
            Beer beer3 = Beer.builder()
                    .beerName("Sunshine City")
                    .beerStyle("IPA")
                    .upc("12356")
                    .price(BigDecimal.valueOf(8.5))
                    .quantityOnHand(144)
                    .build();

            beerRepository.save(beer1).subscribe();
            beerRepository.save(beer2).subscribe();
            beerRepository.save(beer3).subscribe();
        });
    }
}
