package org.example.springbootcrudrest.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "CUSTOMERS", uniqueConstraints = {
        @UniqueConstraint(name = "USERNAME", columnNames = {"USERNAME"})
})
public class Customer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID", nullable = false)
    private Long id;

    @Size(max = 20)
    @NotNull
    @Column(name = "USERNAME", nullable = false, length = 20)
    private String username;

    @NotNull
    @Column(name = "ACTIVE", nullable = false)
    private Boolean active = false;

    @Size(max = 40)
    @Column(name = "FIRSTNAME", length = 40)
    private String firstname;

    @Size(max = 30)
    @NotNull
    @Column(name = "PASSWORD", nullable = false, length = 30)
    private String password;

    @Size(max = 40)
    @Column(name = "LASTNAME", length = 40)
    private String lastname;

    public void setUsername(String username) {
        this.username = username.toLowerCase();
    }

}