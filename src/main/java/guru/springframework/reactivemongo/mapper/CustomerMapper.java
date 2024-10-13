package guru.springframework.reactivemongo.mapper;

import guru.springframework.reactivemongo.domain.Customer;
import guru.springframework.reactivemongo.model.CustomerDTO;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

/**
 * @author john
 * @since 18/09/2024
 */
@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface CustomerMapper {

    Customer customerDTOToCustomer(CustomerDTO customerDTO);

    CustomerDTO customerToCustomerDTO(Customer customer);
}
