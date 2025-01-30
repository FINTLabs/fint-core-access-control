package no.fintlabs.access.control.repository

import jakarta.validation.constraints.NotNull
import no.fintlabs.access.control.model.entity.ClientEntity
import no.fintlabs.access.control.model.entity.PackageAccessEntity
import org.springframework.data.jpa.repository.JpaRepository

interface PackageAccessEntityRepository : JpaRepository<PackageAccessEntity, Int> {
    fun findByClient(client: ClientEntity): Collection<PackageAccessEntity>
}
