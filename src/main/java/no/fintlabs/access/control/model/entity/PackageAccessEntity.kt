package no.fintlabs.access.control.model.entity

import jakarta.persistence.*
import jakarta.validation.constraints.NotNull
import jakarta.validation.constraints.Size

@Entity
@Table(name = "package_access")
class PackageAccessEntity(

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "package_access_id_gen")
    @SequenceGenerator(name = "package_access_id_gen", sequenceName = "packageaccess_packageid_seq", allocationSize = 1)
    @Column(name = "package_id", nullable = false)
    val id: Int? = null,

    @Column(name = "domain_name", nullable = false)
    @field:Size(max = 255)
    @field:NotNull
    val domainName: String = "",

    @Column(name = "package_name", nullable = false)
    @field:Size(max = 255)
    @field:NotNull
    val packageName: String = "",

    @Column(name = "has_fullaccess", nullable = false)
    val hasFullaccess: Boolean = false,

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "username", nullable = false)
    @field:NotNull
    val client: ClientEntity
) {
    // JPA trenger en no-args konstruktør, legg til dette eksplisitt:
    constructor() : this(id = null, domainName = "", packageName = "", hasFullaccess = false, client = ClientEntity())
}
