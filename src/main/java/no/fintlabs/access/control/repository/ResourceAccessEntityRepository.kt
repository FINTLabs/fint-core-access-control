package no.fintlabs.access.control.repository

import no.fintlabs.access.control.model.entity.ResourceAccessEntity
import org.springframework.data.jpa.repository.JpaRepository

interface ResourceAccessEntityRepository : JpaRepository<ResourceAccessEntity, Int> {
    fun findByPackageId(id: Int): Collection<ResourceAccessEntity>
}
