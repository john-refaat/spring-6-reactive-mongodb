package guru.springframework.reactivemongo.service;

import guru.springframework.reactivemongo.model.CustomerDTO;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

/**
 * @author john
 * @since 13/10/2024
 */
@Service
public class CustomerServiceImpl implements CustomerService {
    @Override
    public Mono<CustomerDTO> saveCustomer(CustomerDTO customerDTO) {
        return null;
    }

    @Override
    public Mono<CustomerDTO> findById(String id) {
        return null;
    }
}
