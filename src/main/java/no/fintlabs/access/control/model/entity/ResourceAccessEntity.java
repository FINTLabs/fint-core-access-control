package no.fintlabs.access.control.model.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

@Entity
public class ResourceAccessEntity {

    @Id
    private String resourceId;

    @ManyToOne
    @JoinColumn(name = "packageAccessId")
    private PackageAccessEntity packageAccessEntity;

    private String resourceName;
    private boolean hasAccess;
    private boolean harWriteAccess;
    private boolean hasReadAllAccess;

}
