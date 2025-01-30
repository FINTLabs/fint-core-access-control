package no.fintlabs.access.control.model.entity

import jakarta.persistence.*
import jakarta.validation.constraints.NotNull
import jakarta.validation.constraints.Size

@Entity
@Table(name = "field_access")
class FieldAccessEntity(
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "field_access_id_gen")
    @SequenceGenerator(name = "field_access_id_gen", sequenceName = "fieldaccess_fieldid_seq", allocationSize = 1)
    @Column(name = "field_id", nullable = false)
    val id: Int? = null,

    @Column(name = "resource_id", nullable = false)
    @field:NotNull
    val resourceId: Int = 0,

    @Column(name = "field_name", nullable = false)
    @field:Size(max = 255)
    @field:NotNull
    val fieldName: String = "",

    @Column(name = "must_contain", length = Int.MAX_VALUE)
    val mustContain: String? = null,

    @Column(name = "has_access", nullable = false)
    val hasAccess: Boolean = false
) {
    // JPA krever en no-args konstruktør, så vi legger til en eksplisitt
    constructor() : this(
        id = null,
        resourceId = 0,
        fieldName = "",
        mustContain = null,
        hasAccess = false
    )
}
