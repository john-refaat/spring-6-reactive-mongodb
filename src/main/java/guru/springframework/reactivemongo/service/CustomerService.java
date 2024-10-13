package guru.springframework.reactivemongo.service;

import guru.springframework.reactivemongo.model.CustomerDTO;

/**
 * @author john
 * @since 13/10/2024
 */
public interface CustomerService {
    CustomerDTO saveCustomer(CustomerDTO customerDTO);
    CustomerDTO findById(String id);
}
