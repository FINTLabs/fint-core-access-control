package no.fintlabs.access.control.model.entity;


import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

import java.util.UUID;

@Entity
public class FieldAccessEntity {

    @Id
    private String fieldId;

    @ManyToOne
    @JoinColumn(name = "resourceId")
    private ResourceAccessEntity resource;

    private String fieldName;
    private String mustContain;
    private boolean hasAccess;



}
