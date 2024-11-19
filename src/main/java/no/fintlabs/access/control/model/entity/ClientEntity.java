package no.fintlabs.access.control.model.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;

@Entity
public class ClientEntity {

    @Id
    private String username;
    private boolean isAdapter;



}
