package no.fintlabs.access.control.model.entity

import jakarta.persistence.*
import jakarta.validation.constraints.NotNull
import jakarta.validation.constraints.Size

@Entity
@Table(name = "resource_access")
class ResourceAccessEntity(
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "resource_access_id_gen")
    @SequenceGenerator(
        name = "resource_access_id_gen",
        sequenceName = "resourceaccess_resourceid_seq",
        allocationSize = 1
    )
    @Column(name = "resource_id", nullable = false)
    val id: Int? = null, // JPA trenger at ID kan være null ved persist

    @Column(name = "package_id", nullable = false)
    @field:NotNull
    val packageId: Int = 0,

    @Column(name = "resource_name", nullable = false)
    @field:Size(max = 255)
    @field:NotNull
    val resourceName: String = "",

    @Column(name = "has_access", nullable = false)
    val hasAccess: Boolean = false,

    @Column(name = "has_write_access", nullable = false)
    val hasWriteAccess: Boolean = false,

    @Column(name = "has_read_all_access", nullable = false)
    val hasReadAllAccess: Boolean = false
) {
    // JPA krever en no-args konstruktør, så vi legger til en eksplisitt
    constructor() : this(
        id = null,
        packageId = 0,
        resourceName = "",
        hasAccess = false,
        hasWriteAccess = false,
        hasReadAllAccess = false
    )
}
