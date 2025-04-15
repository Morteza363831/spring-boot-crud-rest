package org.example.springbootcrudrest.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * DTO for {@link Customer}
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
@JsonIgnoreProperties(ignoreUnknown = true)
public class CustomerCreateDto implements Serializable {
    @NotNull(message = "username cannot be null")
    @Size(message = "username maximum size is 20", max = 20)
    private String username;
    @Size(message = "firstname maximum size is 40", max = 40)
    private String firstname;
    @NotNull(message = "password cannot be null")
    @Size(message = "password maximum size is 30", max = 30)
    private String password;
    @Size(message = "lastname maximum size is 40", max = 40)
    private String lastname;
}