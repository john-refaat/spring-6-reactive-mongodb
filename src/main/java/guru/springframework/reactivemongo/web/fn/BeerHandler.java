package guru.springframework.reactivemongo.web.fn;

import guru.springframework.reactivemongo.model.BeerDTO;
import guru.springframework.reactivemongo.model.PatchBeerDTO;
import guru.springframework.reactivemongo.service.BeerService;
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
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * @author john
 * @since 02/11/2024
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class BeerHandler {
    private final BeerService beerService;
    private final Validator validator;

    private void validate(BeerDTO beerDTO) {
        Errors errors = new BeanPropertyBindingResult(beerDTO, "beerDTO");
        validator.validate(beerDTO, errors);
        if (errors.hasErrors()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, errors.toString());
            //throw new ServerWebInputException(errors.toString());
        }
    }

    private void validate(PatchBeerDTO beerDTO) {
        Errors errors = new BeanPropertyBindingResult(beerDTO, "beerDTO");
        validator.validate(beerDTO, errors);
        if (errors.hasErrors()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, errors.toString());
            //throw new ServerWebInputException(errors.toString());
        }
    }

    public Mono<ServerResponse> listBeers(ServerRequest request) {
        log.info("List Beers");
        Flux<BeerDTO> flux;
        if (request.queryParam("style").isPresent()) {
            flux = beerService.findByBeerStyle(request.queryParam("style").get());
        } else {
            flux = beerService.findAll();
        }
        return ServerResponse.ok().body(flux, BeerDTO.class);
    }

    public Mono<ServerResponse> getBeerById(ServerRequest request) {
        return beerService.findById(request.pathVariable("beerId"))
                .switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND, "Beer not found")))
                .flatMap(beerDTO -> ServerResponse.ok().bodyValue(beerDTO));
      /*  return ServerResponse.ok()
                .body(beerService.findById(request.pathVariable("beerId")), BeerDTO.class);*/
    }

    public Mono<ServerResponse> saveBeer(ServerRequest request) {

        return beerService.saveBeer(request.bodyToMono(BeerDTO.class).doOnNext(this::validate))
                .flatMap(beerDTO ->
                        ServerResponse.created(
                                UriComponentsBuilder.fromPath(BeerRouterConfig.BEER_PATH)
                                        .pathSegment(beerDTO.getId()).build().toUri()
                        ).bodyValue(beerDTO));   //
    }

    public Mono<ServerResponse> updateBeer(ServerRequest request) {
        return request.bodyToMono(BeerDTO.class).doOnNext(this::validate)
                .flatMap(beerDTO -> beerService.updateBeer(request.pathVariable("beerId"), beerDTO))
                .flatMap(beerDTO -> ServerResponse.ok().bodyValue(beerDTO));
    }

    public Mono<ServerResponse> patchBeer(ServerRequest request) {
        return request.bodyToMono(PatchBeerDTO.class)
                .doOnNext(this::validate)
                .flatMap(beerDTO -> beerService.patchBeer(request.pathVariable("beerId"), beerDTO))
                .switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND)))
                .flatMap(beerDTO -> {
                    //return ServerResponse.ok().bodyValue(beerDTO);
                    return ServerResponse.noContent().build();
                });
    }

    public Mono<ServerResponse> deleteBeer(ServerRequest request) {
        return beerService.findById(request.pathVariable("beerId"))
                .switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND)))
                .flatMap(beerDTO -> beerService.deleteBeer(beerDTO.getId())
                        .then(ServerResponse.noContent().build()));
    }
}
