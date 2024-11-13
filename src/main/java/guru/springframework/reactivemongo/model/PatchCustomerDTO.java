package guru.springframework.reactivemongo.model;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author john
 * @since 13/11/2024
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PatchCustomerDTO {

    @Size(min=3, max=50)
    private String firstName;

    @Size(min=3, max=50)
    private String lastName;

    @Size(min=5, max=100)
    @Email
    private String email;
}
