package guru.springframework.reactivemongo.bootstrap;

import guru.springframework.reactivemongo.domain.Beer;
import guru.springframework.reactivemongo.domain.Customer;
import guru.springframework.reactivemongo.repository.BeerRepository;
import guru.springframework.reactivemongo.repository.CustomerRepository;
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

    private final CustomerRepository customerRepository;

    @Override
    public void run(String... args) throws Exception {
        beerRepository.deleteAll().doOnSuccess(foo -> {
            loadBeerData();
        }).subscribe();

        customerRepository.deleteAll().doOnSuccess(foo -> {
            loadCustomerData();
        }).subscribe();

    }

    private void loadCustomerData() {
        log.info("Load Customer Data");
        customerRepository.count().filter(count -> count==0).subscribe(count -> {
            log.info("Loading customer data");
            customerRepository.save(Customer.builder()
                   .firstName("John")
                   .lastName("Doe")
                   .email("john.doe@example.com")
                   .build()).subscribe();
            customerRepository.save(Customer.builder()
                   .firstName("Jane")
                   .lastName("Smith")
                   .email("jane.smith@example.com")
                   .build()).subscribe();
            customerRepository.save(Customer.builder()
                   .firstName("Bob")
                   .lastName("Johnson")
                   .email("bob.johnson@example.com")
                   .build()).subscribe();
            customerRepository.save(Customer.builder()
                   .firstName("Alice")
                   .lastName("Williams")
                   .email("alice.williams@example.com")
                   .build()).subscribe();
            log.info("Customer data loaded");
        });
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
