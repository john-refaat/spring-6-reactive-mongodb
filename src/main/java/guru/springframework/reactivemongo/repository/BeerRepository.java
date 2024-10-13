package guru.springframework.reactivemongo.repository;

import guru.springframework.reactivemongo.domain.Beer;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

/**
 * @author john
 * @since 13/10/2024
 */
public interface BeerRepository extends ReactiveMongoRepository<Beer, String> {

}