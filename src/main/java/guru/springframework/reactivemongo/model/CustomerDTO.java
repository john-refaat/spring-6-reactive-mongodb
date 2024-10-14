package guru.springframework.reactivemongo.model;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * @author john
 * @since 18/09/2024
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CustomerDTO {
    private String id;

    @NotBlank
    @Size(min=3, max=50)
    private String firstName;

    @NotBlank
    @Size(min=3, max=50)
    private String lastName;

    @NotBlank
    @Size(min=5, max=100)
    @Email
    private String email;

    private LocalDateTime createdDate;

    private LocalDateTime lastModifiedDate;

    public String getFullName() {
        return firstName + " " + lastName;
    }

}
