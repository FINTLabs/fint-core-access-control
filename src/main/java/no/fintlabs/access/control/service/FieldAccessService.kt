package no.fintlabs.access.control.service

import no.fintlabs.access.control.model.dto.FieldAccess
import no.fintlabs.access.control.model.entity.FieldAccessEntity
import no.fintlabs.access.control.model.entity.ResourceAccessEntity
import no.fintlabs.access.control.model.metamodel.Metamodel
import no.fintlabs.access.control.repository.FieldAccessEntityRepository
import org.springframework.stereotype.Service

@Service
class FieldAccessService(private val fieldAccessEntityRepository: FieldAccessEntityRepository) {
    fun getFieldAccess(metamodel: Metamodel, resourceAccessEntity: ResourceAccessEntity?): List<FieldAccess> {
        val result: MutableList<FieldAccess> = ArrayList(metamodel.fields.size)
        val fieldAccessEntities = getFieldAccessEntities(resourceAccessEntity)

        for (field in metamodel.fields) {
            val fieldAccessEntity = getFieldAccessEntity(field, fieldAccessEntities)

            result.add(
                FieldAccess(
                    field,
                    shouldContainConverter(fieldAccessEntity),
                    getIsHidden(fieldAccessEntity)
                )
            )
        }

        return result
    }

    private fun getFieldAccessEntities(resourceAccessEntity: ResourceAccessEntity?): Collection<FieldAccessEntity> {
        val resourceId = resourceAccessEntity?.id ?: return emptyList()
        return fieldAccessEntityRepository.findByResourceId(resourceId)
    }

    private fun getIsHidden(fieldAccessEntity: FieldAccessEntity?): Boolean {
        return fieldAccessEntity?.hasAccess?.not() ?: false
    }

    private fun shouldContainConverter(fieldAccessEntity: FieldAccessEntity?): List<String> {
        val input = fieldAccessEntity?.mustContain?.takeIf { it.isNotBlank() } ?: return emptyList()
        return input.split(",").map { it.trim() }
    }

    private fun getFieldAccessEntity(
        field: String,
        fieldAccessEntities: Collection<FieldAccessEntity?>
    ): FieldAccessEntity? {
        return fieldAccessEntities.firstOrNull { it?.fieldName.equals(field, ignoreCase = true) }
    }
}
