package guru.springframework.reactivemongo.model;

import jakarta.validation.constraints.*;
import lombok.*;

import java.math.BigDecimal;

/**
 * @author john
 * @since 10/11/2024
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PatchBeerDTO {

    @Size(min=3, max=255, message = "Name length must be between 3 and 255 characters")
    private String beerName;

    @Size(min=3, max=255)
    private String beerStyle;

    @Size(min=3, max=255)
    private String upc;

    @Min(1)
    @Max(999)
    private Integer quantityOnHand;

    @Positive
    private BigDecimal price;
}
