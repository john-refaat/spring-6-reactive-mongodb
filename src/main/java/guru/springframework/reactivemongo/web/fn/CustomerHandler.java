package guru.springframework.reactivemongo.web.fn;

import guru.springframework.reactivemongo.model.BeerDTO;
import guru.springframework.reactivemongo.model.CustomerDTO;
import guru.springframework.reactivemongo.model.PatchCustomerDTO;
import guru.springframework.reactivemongo.service.CustomerService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.util.UriComponentsBuilder;
import reactor.core.publisher.Mono;

import java.net.URI;

/**
 * @author john
 * @since 11/11/2024
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class CustomerHandler {

    private final CustomerService customerService;

    private final Validator validator;

    private void validate(CustomerDTO customerDTO) {
        Errors errors = new BeanPropertyBindingResult(customerDTO, "customerDTO");
        validator.validate(customerDTO, errors);
        if (errors.hasErrors()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, errors.toString());
        }
    }

    private void validate(PatchCustomerDTO patchCustomerDTO) {
        Errors errors = new BeanPropertyBindingResult(patchCustomerDTO, "patchCustomerDTO");
        validator.validate(patchCustomerDTO, errors);
        if (errors.hasErrors()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, errors.toString());
        }
    }

    public Mono<ServerResponse> listCustomers(ServerRequest request) {
        log.info("listCustomers");
        if(request.queryParam("firstName").isPresent())
            return ServerResponse.ok().body(customerService.findByFirstName(request.queryParam("firstName").get()), CustomerDTO.class);
        
        if(request.queryParam("lastName").isPresent())
            return ServerResponse.ok().body(customerService.findByLastName(request.queryParam("lastName").get()), CustomerDTO.class);
        
        return ServerResponse.ok().body(customerService.findAll(), CustomerDTO.class);
    }

    public Mono<ServerResponse> getCustomerById(ServerRequest request) {
        log.info("getCustomerById");
        return customerService.findById(request.pathVariable("customerId"))
                .switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND)))
                .flatMap(customerDTO -> ServerResponse.ok().body(Mono.just(customerDTO), CustomerDTO.class));
    }

    public Mono<ServerResponse> saveCustomer(ServerRequest request) {
        log.info("saveCustomer");
        return customerService.saveCustomer(request.bodyToMono(CustomerDTO.class).doOnNext(this::validate))
               .flatMap(customerDTO -> ServerResponse.created(
                               UriComponentsBuilder.fromPath(CustomerRouterConfig.CUSTOMER_PATH)
                                       .pathSegment(customerDTO.getId()).build().toUri())
                       .body(Mono.just(customerDTO), CustomerDTO.class));
    }

    public Mono<ServerResponse> updateCustomer(ServerRequest request) {
        log.info("updateCustomer");
        return request.bodyToMono(CustomerDTO.class).doOnNext(this::validate)
                .flatMap(customerDTO -> customerService.updateCustomer(request.pathVariable("customerId"), customerDTO))
                .switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND)))
                .flatMap(customerDTO -> ServerResponse.ok().bodyValue(customerDTO));
    }

    public Mono<ServerResponse> patchCustomer(ServerRequest request) {
        log.info("patchCustomer");
        return request.bodyToMono(PatchCustomerDTO.class)
                .doOnNext(this::validate)
                .flatMap(customerDTO -> customerService.patchCustomer(request.pathVariable("customerId"), customerDTO))
                .switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND)))
               .flatMap(customerDTO -> ServerResponse.ok().bodyValue(customerDTO));
    }

    public Mono<ServerResponse> deleteCustomer(ServerRequest request) {
        log.info("deleteCustomer");
        return customerService.findById(request.pathVariable("customerId"))
                .switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND)))
                .flatMap(customerDTO -> customerService.deleteCustomer(request.pathVariable("customerId"))
                       .then(ServerResponse.noContent().build()));
    }
}

