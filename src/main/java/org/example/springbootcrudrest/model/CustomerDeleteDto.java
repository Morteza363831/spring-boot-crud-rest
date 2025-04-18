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
public class CustomerDeleteDto implements Serializable {
    @NotNull(message = "username cannot be null")
    @Size(message = "username maximum size is 20", max = 20)
    private String username;
}