package no.fintlabs.access.control.model.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "field_access")
public class FieldAccessEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "field_access_id_gen")
    @SequenceGenerator(name = "field_access_id_gen", sequenceName = "fieldaccess_fieldid_seq", allocationSize = 1)
    @Column(name = "field_id", nullable = false)
    private Integer id;

    @NotNull
    @Column(name = "resource_id", nullable = false)
    private Integer resourceId;

    @Size(max = 255)
    @NotNull
    @Column(name = "field_name", nullable = false)
    private String fieldName;

    @Column(name = "must_contain", length = Integer.MAX_VALUE)
    private String mustContain;

    @NotNull
    @Column(name = "has_access", nullable = false)
    private Boolean hasAccess = false;

}