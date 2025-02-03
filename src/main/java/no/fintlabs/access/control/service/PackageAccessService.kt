package no.fintlabs.access.control.service

import no.fintlabs.access.control.model.dto.PackageAccess
import no.fintlabs.access.control.model.dto.Status
import no.fintlabs.access.control.model.entity.ClientEntity
import no.fintlabs.access.control.model.entity.PackageAccessEntity
import no.fintlabs.access.control.model.entity.ResourceAccessEntity
import no.fintlabs.access.control.model.metamodel.MetamodelRepository
import no.fintlabs.access.control.model.metamodel.Package
import no.fintlabs.access.control.repository.ClientEntityRepository
import no.fintlabs.access.control.repository.PackageAccessEntityRepository
import no.fintlabs.access.control.repository.ResourceAccessEntityRepository
import org.springframework.stereotype.Service

@Service
class PackageAccessService(
    private val clientEntityRepository: ClientEntityRepository,
    private val packageAccessEntityRepository: PackageAccessEntityRepository,
    private val resourceAccessEntityRepository: ResourceAccessEntityRepository,
    private val metamodelRepository: MetamodelRepository
) {


    fun getPackageAccess(clientOrAdapterName: String): Collection<PackageAccess> {
        val clientEntity = clientEntityRepository.findById(clientOrAdapterName).orElse(null)
        val entities = getPackageAccessEntities(clientEntity)
        val result: MutableList<PackageAccess> = ArrayList()

        for (aPackage in metamodelRepository.packages) {
            val packageAccessEntity = findPackageAccessEntity(aPackage, entities)
            var status = Status.DISABLED

            if (packageAccessEntity != null) {
                if (packageAccessEntity.hasFullaccess) {
                    status = Status.ENABLED
                } else if (hasAnyPackageAccess(packageAccessEntity)) {
                    status = Status.PARTIAL
                }
            }

            result.add(PackageAccess(aPackage.domainName, aPackage.packageName, status))
        }

        return result
    }

    private fun getPackageAccessEntities(clientEntity: ClientEntity?): Collection<PackageAccessEntity> {
        return clientEntity?.let { packageAccessEntityRepository.findByClient(it) } ?: emptyList()
    }

    private fun findPackageAccessEntity(
        aPackage: Package,
        entities: Collection<PackageAccessEntity>
    ): PackageAccessEntity? {
        return entities.firstOrNull { it.domainName == aPackage.domainName && it.packageName == aPackage.packageName }
    }

    private fun hasAnyPackageAccess(packageAccessEntity: PackageAccessEntity?): Boolean {
        val resources = packageAccessEntity?.id?.let { resourceAccessEntityRepository.findByPackageId(it) } ?: emptyList()
        return resources.any { it.hasAccess }
    }
}
