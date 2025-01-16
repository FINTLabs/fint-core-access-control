package no.fintlabs.access.control.model.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "client")
public class ClientEntity {
    @Id
    @Size(max = 255)
    @Column(name = "username", nullable = false)
    private String username;

    @NotNull
    @Column(name = "is_adapter", nullable = false)
    private Boolean isAdapter = false;

    @Size(max = 1000)
    @Column(name = "allowed_environments", length = 1000)
    private String allowedEnvironments;

}