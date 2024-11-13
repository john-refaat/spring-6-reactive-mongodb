package guru.springframework.reactivemongo.web.fn;

import guru.springframework.reactivemongo.domain.Customer;
import guru.springframework.reactivemongo.mapper.CustomerMapper;
import guru.springframework.reactivemongo.model.CustomerDTO;
import guru.springframework.reactivemongo.repository.CustomerRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.reactive.server.EntityExchangeResult;
import org.springframework.test.web.reactive.server.WebTestClient;

import java.util.List;
import java.util.Map;

/**
 * @author john
 * @since 13/11/2024
 */
@SpringBootTest
@AutoConfigureWebTestClient
public class CustomerEndpointTest {

    @Autowired
    WebTestClient client;

    @Autowired
    CustomerRepository customerRepository;

    @Autowired
    CustomerMapper customerMapper;

    private CustomerDTO customerDTO;

    @BeforeEach
    void setUp() {
        customerDTO = CustomerDTO.builder()
                .firstName("Rebecca")
                .lastName("Smith")
                .email("rebecca.smith@example.com")
                .build();
    }

    @Test
    void testListCustomers() {

        client.get().uri("/api/v3/customer")
               .exchange()
               .expectStatus().isOk()
               .expectBodyList(CustomerDTO.class);
    }

    @Test
    void testGetCustomerById() {
        Customer savedCustomer = saveCustomer();
        client.get().uri("/api/v3/customer/{customerId}", savedCustomer.getId())
                .exchange()
                .expectStatus().isOk()
                .expectBody().jsonPath("$.id").isEqualTo(savedCustomer.getId())
                .jsonPath("$.firstName").isEqualTo(savedCustomer.getFirstName())
                .jsonPath("$.lastName").isEqualTo(savedCustomer.getLastName())
                .jsonPath("$.email").isEqualTo(savedCustomer.getEmail());
    }

    @Test
    void testGetCustomerByIdNotFound() {
        client.get().uri("/api/v3/customer/{customerId}", "not-found")
               .exchange()
               .expectStatus().isNotFound();
    }

    @Test
    void testSaveCustomer() {
        client.post().uri("/api/v3/customer")
               .bodyValue(customerDTO)
               .exchange()
               .expectStatus().isCreated()
               .expectBody().jsonPath("$.id").exists()
                .jsonPath("$.firstName").isEqualTo(customerDTO.getFirstName())
                .jsonPath("$.lastName").isEqualTo(customerDTO.getLastName())
                .jsonPath("$.email").isEqualTo(customerDTO.getEmail());
    }

    @Test
    void testSaveCustomerBadRequest() {
        client.post().uri("/api/v3/customer")
               .bodyValue(Map.of("firstName", "", "lastName", "Smith", "email", "invalid-email"))
               .exchange()
               .expectStatus().isBadRequest();
    }

    @Test
    void testUpdateCustomer() {
        Customer savedCustomer = saveCustomer();

        client.put().uri("/api/v3/customer/{customerId}", savedCustomer.getId())
               .bodyValue(customerDTO)
               .exchange()
               .expectStatus().isOk()
               .expectBody().jsonPath("$.id").isEqualTo(savedCustomer.getId())
               .jsonPath("$.firstName").isEqualTo(customerDTO.getFirstName())
               .jsonPath("$.lastName").isEqualTo(customerDTO.getLastName())
               .jsonPath("$.email").isEqualTo(customerDTO.getEmail());
    }

    @Test
    void testUpdateCustomerNotFound() {
        client.put().uri("/api/v3/customer/{customerId}", "not-found")
               .bodyValue(customerDTO)
               .exchange()
               .expectStatus().isNotFound();
    }

    @Test
    void testUpdateCustomerBadRequest() {
        client.put().uri("/api/v3/customer/{customerId}", "not-found")
               .bodyValue(Map.of("firstName", "XAX", "lastName", "Smith", "email", "invalid-email"))
               .exchange()
               .expectStatus().isBadRequest();
    }

    @Test
    void testPatchCustomer() {
        Customer savedCustomer = saveCustomer();

        client.patch().uri("/api/v3/customer/{customerId}", savedCustomer.getId())
               .bodyValue(Map.of("firstName", "Sally"))
               .exchange()
               .expectStatus().isOk()
               .expectBody().jsonPath("$.id").isEqualTo(savedCustomer.getId())
               .jsonPath("$.firstName").isEqualTo("Sally")
               .jsonPath("$.lastName").isEqualTo(savedCustomer.getLastName())
               .jsonPath("$.email").isEqualTo(savedCustomer.getEmail());
    }

    @Test
    void testPatchCustomerNotFound() {
        client.patch().uri("/api/v3/customer/{customerId}", "not-found")
               .bodyValue(Map.of("firstName", "Sally"))
               .exchange()
               .expectStatus().isNotFound();
    }

    @Test
    void testPatchCustomerBadRequest() {
       Customer savedCustomer = saveCustomer();
        client.patch().uri("/api/v3/customer/{customerId}", savedCustomer.getId())
                .bodyValue(Map.of("email", "X"))
               .exchange().expectStatus().isBadRequest();
    }

    @Test
    void testDeleteCustomer() {
        Customer savedCustomer = saveCustomer();

        client.delete().uri("/api/v3/customer/{customerId}", savedCustomer.getId())
               .exchange()
               .expectStatus().isNoContent();

        Assertions.assertNull(customerRepository.findById(savedCustomer.getId()).block());
    }

    @Test
    void testDeleteCustomerNotFound() {
        client.delete().uri("/api/v3/customer/{customerId}", "not-found")
               .exchange()
               .expectStatus().isNotFound();
    }

    @Test
    void testSearchCustomerByFirstName() {
        Customer savedCustomer = saveCustomer();

        client.get().uri("/api/v3/customer?firstName=John")
                .exchange()
                .expectStatus().isOk()
                .expectBody().jsonPath("[0].firstName").isEqualTo("John");
    }

    @Test
    void testSearchCustomerByLastName() {
        Customer savedCustomer = saveCustomer();

        client.get().uri("/api/v3/customer?lastName=Stefanos")
                .exchange()
                .expectStatus().isOk()
                .expectBody().jsonPath("$.[0].lastName").isEqualTo("Stefanos");
    }

    private Customer saveCustomer() {
        return customerRepository.save(Customer.builder()
                .firstName("John")
                .lastName("Stefanos")
                .email("john.stefanos@example.com")
                .build()).block();
    }
}

