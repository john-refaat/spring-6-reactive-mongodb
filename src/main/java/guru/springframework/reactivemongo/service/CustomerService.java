package guru.springframework.reactivemongo.service;

import guru.springframework.reactivemongo.model.CustomerDTO;
import reactor.core.publisher.Mono;

/**
 * @author john
 * @since 13/10/2024
 */
public interface CustomerService {
    Mono<CustomerDTO> saveCustomer(CustomerDTO customerDTO);
    Mono<CustomerDTO> findById(String id);
}
