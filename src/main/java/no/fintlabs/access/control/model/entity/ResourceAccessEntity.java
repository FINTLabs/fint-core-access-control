package no.fintlabs.access.control.model.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "resource_access")
public class ResourceAccessEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "resource_access_id_gen")
    @SequenceGenerator(name = "resource_access_id_gen", sequenceName = "resourceaccess_resourceid_seq", allocationSize = 1)
    @Column(name = "resource_id", nullable = false)
    private Integer id;

    @NotNull
    @Column(name = "package_id", nullable = false)
    private Integer packageId;

    @Size(max = 255)
    @NotNull
    @Column(name = "resource_name", nullable = false)
    private String resourceName;

    @NotNull
    @Column(name = "has_access", nullable = false)
    private Boolean hasAccess = false;

    @NotNull
    @Column(name = "has_write_access", nullable = false)
    private Boolean hasWriteAccess = false;

    @NotNull
    @Column(name = "has_read_all_access", nullable = false)
    private Boolean hasReadAllAccess = false;

}