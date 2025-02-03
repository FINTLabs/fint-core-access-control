package no.fintlabs.access.control.model.entity

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.Table
import jakarta.validation.constraints.Size

@Entity
@Table(name = "client")
class ClientEntity(
    @Id
    @Column(name = "username", nullable = false)
    @field:Size(max = 255)
    val username: String,

    @Column(name = "is_adapter", nullable = false)
    val isAdapter: Boolean = false,

    @Column(name = "allowed_environments", length = 1000)
    @field:Size(max = 1000)
    val allowedEnvironments: String? = null
) {
    // JPA krever en no-args konstruktør, så vi legger til en eksplisitt
    constructor() : this(
        username = "",
        isAdapter = false,
        allowedEnvironments = null
    )
}
