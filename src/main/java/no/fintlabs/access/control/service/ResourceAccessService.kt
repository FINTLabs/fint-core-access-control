package no.fintlabs.access.control.service

import no.fintlabs.access.control.model.dto.ReadingOption
import no.fintlabs.access.control.model.dto.ResourceAccess
import no.fintlabs.access.control.model.entity.ClientEntity
import no.fintlabs.access.control.model.entity.PackageAccessEntity
import no.fintlabs.access.control.model.entity.ResourceAccessEntity
import no.fintlabs.access.control.model.metamodel.Metamodel
import no.fintlabs.access.control.model.metamodel.MetamodelRepository
import no.fintlabs.access.control.repository.ClientEntityRepository
import no.fintlabs.access.control.repository.PackageAccessEntityRepository
import no.fintlabs.access.control.repository.ResourceAccessEntityRepository
import org.springframework.stereotype.Service
import java.util.*

@Service
class ResourceAccessService(
    private val clientEntityRepository: ClientEntityRepository,
    private val resourceAccessEntityRepository: ResourceAccessEntityRepository,
    private val packageAccessEntityRepository: PackageAccessEntityRepository,
    private val metamodelRepository: MetamodelRepository,
    private val fieldAccessService: FieldAccessService
) {
    fun getResourceAccess(clientOrAdapterName: String, component: String): Collection<ResourceAccess> {
        val client = clientEntityRepository.findById(clientOrAdapterName).orElse(null)
        val packageAccessEntities = getPackageAccessEntities(client)
        val packageAccessEntity = getAccessEntity(component, packageAccessEntities)
        val resourceAccessEntities = getResourceAccessEntities(packageAccessEntity)
        val result: MutableList<ResourceAccess> = ArrayList()

        for (metamodel in metamodelRepository.getResourceAccessByComponent(component)) {
            result.add(createResourceAccess(metamodel, resourceAccessEntities))
        }

        return result
    }

    private fun createResourceAccess(
        metamodel: Metamodel,
        resourceAccessEntities: Collection<ResourceAccessEntity?>
    ): ResourceAccess {
        val resourceAccessEntity = getResourceAccessEntity(metamodel, resourceAccessEntities)
        val fieldAccesses = fieldAccessService.getFieldAccess(metamodel, resourceAccessEntity)

        if (resourceAccessEntity == null) {
            return ResourceAccess(
                metamodel.resourceName,
                fieldAccesses,
                ReadingOption.SINGULAR,
                false,
                false
            )
        }

        return ResourceAccess(
            metamodel.resourceName,
            fieldAccesses,
            if (resourceAccessEntity.hasReadAllAccess) ReadingOption.MULTIPLE else ReadingOption.SINGULAR,
            resourceAccessEntity.hasAccess,
            resourceAccessEntity.hasWriteAccess
        )
    }

    private fun getResourceAccessEntities(packageAccessEntity: PackageAccessEntity?): Collection<ResourceAccessEntity> {
        if (packageAccessEntity?.id == null) return emptyList()
        return resourceAccessEntityRepository.findByPackageId(packageAccessEntity.id)
    }

    private fun getPackageAccessEntities(client: ClientEntity?): Collection<PackageAccessEntity> {
        if (client == null) return emptyList()
        return packageAccessEntityRepository.findByClient(client)
    }

    private fun getAccessEntity(
        component: String,
        packageAccessEntities: Collection<PackageAccessEntity>
    ): PackageAccessEntity? {
        for (packageAccessEntity in packageAccessEntities) {

            val currentComponent = packageAccessEntity.domainName + "-" + packageAccessEntity.packageName
            if (component == currentComponent) {
                return packageAccessEntity
            }
        }

        return null
    }

    private fun getResourceAccessEntity(
        metamodel: Metamodel,
        resourceAccessEntities: Collection<ResourceAccessEntity?>
    ): ResourceAccessEntity? {
        return resourceAccessEntities.firstOrNull { it?.resourceName == metamodel.resourceName }
    }
}
