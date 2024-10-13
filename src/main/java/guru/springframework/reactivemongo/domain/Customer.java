package guru.springframework.reactivemongo.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

/**
 * @author john
 * @since 18/09/2024
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Document
public class Customer {

    @Id
    private String id;
    private String firstName;
    private String lastName;
    private String email;

    @CreatedDate
    private LocalDateTime createdDate;

    @LastModifiedDate
    private LocalDateTime lastModifiedDate;

    public String getFullName() {
        return firstName + " " + lastName;
    }
}
