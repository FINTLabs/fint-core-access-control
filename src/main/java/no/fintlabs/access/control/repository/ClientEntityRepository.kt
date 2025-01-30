package no.fintlabs.access.control.repository

import no.fintlabs.access.control.model.entity.ClientEntity
import org.springframework.data.jpa.repository.JpaRepository

interface ClientEntityRepository : JpaRepository<ClientEntity, String>
