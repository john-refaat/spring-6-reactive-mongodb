package guru.springframework.reactivemongo.service;

import guru.springframework.reactivemongo.mapper.CustomerMapper;
import guru.springframework.reactivemongo.model.CustomerDTO;
import guru.springframework.reactivemongo.model.PatchCustomerDTO;
import guru.springframework.reactivemongo.repository.CustomerRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * @author john
 * @since 13/10/2024
 */
@Slf4j
@RequiredArgsConstructor
@Service
public class CustomerServiceImpl implements CustomerService {

    private final CustomerRepository customerRepository;

    private final CustomerMapper customerMapper;

    @Override
    public Mono<CustomerDTO> saveCustomer(Mono<CustomerDTO> customerDTO) {
        return customerDTO.map(customerMapper::customerDTOToCustomer)
                .flatMap(customerRepository::save)
                .map(customerMapper::customerToCustomerDTO);
    }

    @Override
    public Mono<CustomerDTO> saveCustomer(CustomerDTO customerDTO) {
        log.info("Saving customer object {}", customerDTO);
        return customerRepository.save(customerMapper.customerDTOToCustomer(customerDTO))
                .map(customerMapper::customerToCustomerDTO);
    }

    @Override
    public Mono<CustomerDTO> findById(String id) {
        log.info("Finding customer by id {}", id);
        return customerRepository.findById(id).map(customerMapper::customerToCustomerDTO);
    }

    @Override
    public Flux<CustomerDTO> findAll() {
        log.info("Finding all customers");
        return customerRepository.findAll().map(customerMapper::customerToCustomerDTO);
    }

    @Override
    public Mono<CustomerDTO> updateCustomer(String id, CustomerDTO customer) {
        log.info("Updating customer by id {} with {}", id, customer);
        return customerRepository.findById(id).map(foundCustomer -> {
            foundCustomer.setFirstName(customer.getFirstName());
            foundCustomer.setLastName(customer.getLastName());
            foundCustomer.setEmail(customer.getEmail());
            return foundCustomer;
        }).flatMap(customerRepository::save).map(customerMapper::customerToCustomerDTO);
    }

    @Override
    public Mono<CustomerDTO> patchCustomer(String id, PatchCustomerDTO customer) {
        log.info("Patching customer by id {} with {}", id, customer);
        return customerRepository.findById(id).map(foundCustomer -> {
            foundCustomer.setFirstName(customer.getFirstName() != null ? customer.getFirstName() : foundCustomer.getFirstName());
            foundCustomer.setLastName(customer.getLastName() != null ? customer.getLastName() : foundCustomer.getLastName());
            foundCustomer.setEmail(customer.getEmail()!= null? customer.getEmail() : foundCustomer.getEmail());
            return foundCustomer;
        }).flatMap(customerRepository::save).map(customerMapper::customerToCustomerDTO);
    }

    @Override
    public Mono<Void> deleteCustomer(String id) {
        log.info("Deleting Customer by id {}", id);
        return customerRepository.deleteById(id);
    }

    @Override
    public Mono<CustomerDTO> findFirstByFirstName(String firstName) {
        log.info("Finding One customer by first name {}", firstName);
        return customerRepository.findFirstByFirstName(firstName)
                .map(customerMapper::customerToCustomerDTO);
    }

    @Override
    public Flux<CustomerDTO> findByFirstName(String firstName) {
        log.info("Finding customers by first name {}", firstName);
        return customerRepository.findByFirstNameLikeIgnoreCase(firstName)
                .map(customerMapper::customerToCustomerDTO);
    }

    @Override
    public Flux<CustomerDTO> findByLastName(String lastName) {
        log.info("Finding customers by last name {}", lastName);
        return customerRepository.findByLastNameLikeIgnoreCase(lastName)
                .map(customerMapper::customerToCustomerDTO);
    }
}
