package no.fintlabs.access.control.model.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;

@Entity
public class PackageAccessEntity {

    @Id
    private String packageAccessId;
    private String domainName;
    private String packageName;
    private boolean hasFullAccess;
    private String username;
}
