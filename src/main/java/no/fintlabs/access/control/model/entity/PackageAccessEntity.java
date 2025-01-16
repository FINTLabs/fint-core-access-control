package no.fintlabs.access.control.model.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "package_access")
public class PackageAccessEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "package_access_id_gen")
    @SequenceGenerator(name = "package_access_id_gen", sequenceName = "packageaccess_packageid_seq", allocationSize = 1)
    @Column(name = "package_id", nullable = false)
    private Integer id;

    @Size(max = 255)
    @NotNull
    @Column(name = "domain_name", nullable = false)
    private String domainName;

    @Size(max = 255)
    @NotNull
    @Column(name = "package_name", nullable = false)
    private String packageName;

    @NotNull
    @Column(name = "has_fullaccess", nullable = false)
    private Boolean hasFullaccess = false;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "username", nullable = false)
    private ClientEntity client;

}