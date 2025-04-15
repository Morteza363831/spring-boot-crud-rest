package org.example.springbootcrudrest.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.io.Serializable;

/**
 * DTO for {@link Customer}
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public record CustomerResultDto(Long id, @NotNull @Size(max = 20) String username, @Size(max = 40) String firstname, @Size(max = 40) String lastname) implements Serializable {
  }