package guru.springframework.reactivemongo.model;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * @author john
 * @since 14/09/2024
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BeerDTO {

    private Integer id;

    @NotBlank
    @Size(min=3, max=255, message = "Name length must be between 3 and 255 characters")
    private String beerName;

    @NotBlank
    @Size(min=3, max=255)
    private String beerStyle;

    @NotBlank
    @Size(min=3, max=255)
    private String upc;

    @NotNull
    @Min(1)
    @Max(999)
    private Integer quantityOnHand;

    @NotNull
    @Positive
    private BigDecimal price;

    private LocalDateTime createdDate;

    private LocalDateTime lastModifiedDate;
}
