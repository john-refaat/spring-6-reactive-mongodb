package guru.springframework.reactivemongo.service;

import guru.springframework.reactivemongo.model.CustomerDTO;
import guru.springframework.reactivemongo.model.PatchCustomerDTO;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * @author john
 * @since 13/10/2024
 */
public interface CustomerService {
    Mono<CustomerDTO> saveCustomer(Mono<CustomerDTO> customerDTO);
    Mono<CustomerDTO> saveCustomer(CustomerDTO customerDTO);
    Mono<CustomerDTO> findById(String id);
    Flux<CustomerDTO> findAll();
    Mono<CustomerDTO> updateCustomer(String id, CustomerDTO customer);
    Mono<CustomerDTO> patchCustomer(String id, PatchCustomerDTO customer);
    Mono<Void> deleteCustomer(String id);
    Mono<CustomerDTO> findFirstByFirstName(String email);
    Flux<CustomerDTO> findByFirstName(String firstName);
    Flux<CustomerDTO> findByLastName(String lastName);
}
