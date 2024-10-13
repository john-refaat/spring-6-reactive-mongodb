package guru.springframework.reactivemongo.repository;

import guru.springframework.reactivemongo.domain.Customer;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

/**
 * @author john
 * @since 13/10/2024
 */
public interface CustomerRepository extends ReactiveMongoRepository<Customer, String> {

}
