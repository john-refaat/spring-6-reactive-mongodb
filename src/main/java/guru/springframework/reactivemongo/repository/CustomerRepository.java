package guru.springframework.reactivemongo.repository;

import guru.springframework.reactivemongo.domain.Customer;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * @author john
 * @since 13/10/2024
 */
public interface CustomerRepository extends ReactiveMongoRepository<Customer, String> {

    Mono<Customer> findFirstByFirstName(String firstName);
    Flux<Customer> findByFirstNameLikeIgnoreCase(String firstName);
    Flux<Customer> findByLastNameLikeIgnoreCase(String lastName);
}
