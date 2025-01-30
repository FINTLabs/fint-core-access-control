package no.fintlabs.access.control.repository

import no.fintlabs.access.control.model.entity.FieldAccessEntity
import org.springframework.data.jpa.repository.JpaRepository

interface FieldAccessEntityRepository : JpaRepository<FieldAccessEntity, Int> {
    fun findByResourceId(id: Int): Collection<FieldAccessEntity>
}
