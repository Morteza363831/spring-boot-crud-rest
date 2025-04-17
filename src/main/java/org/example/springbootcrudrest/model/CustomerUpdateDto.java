package org.example.springbootcrudrest.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * DTO for {@link Customer} -> mutable
 */
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
@JsonIgnoreProperties(ignoreUnknown = true)
public class CustomerUpdateDto implements Serializable {
    @Size(message = "username maximum size is 20", max = 20)
    private String username;
    @Size(message = "firstname maximum size is 40", max = 40)
    private String firstname;
    @Size(message = "password maximum size is 30", max = 30)
    private String password;
    @Size(message = "lastname maximum size is 40", max = 40)
    private String lastname;
}