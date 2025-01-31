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
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.ArgumentMatchers
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.MockitoAnnotations
import java.util.*
import java.util.function.Consumer

internal class PackageAccessServiceTest {

    @Mock
    lateinit var clientEntityRepository: ClientEntityRepository

    @Mock
    lateinit var packageAccessEntityRepository: PackageAccessEntityRepository

    @Mock
    lateinit var resourceAccessEntityRepository: ResourceAccessEntityRepository

    @Mock
    lateinit var metamodelRepository: MetamodelRepository

    lateinit var packageAccessService: PackageAccessService

    val elevPackage = Package("utdanning", "elev")

    val timeplanPackage = Package("utdanning", "timeplan")


    @BeforeEach
    fun setUp() {
        MockitoAnnotations.openMocks(this)
        packageAccessService = PackageAccessService(
            clientEntityRepository,
            packageAccessEntityRepository,
            resourceAccessEntityRepository,
            metamodelRepository
        )
    }

    @Test
    fun packageAccessWithNoAccess() {
        val clientName = "test-client@fintlabs.no"
        Mockito.`when`(clientEntityRepository.findById(clientName)).thenReturn(Optional.empty())
        Mockito.`when`(
            metamodelRepository.packages
        ).thenReturn(setOf(elevPackage, timeplanPackage))

        val result = packageAccessService.getPackageAccess(clientName)

        Assertions.assertEquals(2, result.size)
        result.forEach(Consumer { access: PackageAccess ->
            Assertions.assertEquals(
                Status.DISABLED,
                access.status
            )
        })
        Mockito.verify(clientEntityRepository).findById(clientName)
        Mockito.verify(metamodelRepository).packages
        Mockito.verifyNoInteractions(packageAccessEntityRepository, resourceAccessEntityRepository)
    }

    @Test
    fun packageAccessWithFullAccess() {
        val clientName = "test-client"
        val clientEntity = ClientEntity()
        Mockito.`when`(clientEntityRepository.findById(clientName)).thenReturn(Optional.of(clientEntity))
        Mockito.`when`(packageAccessEntityRepository.findByClient(clientEntity)).thenReturn(
            listOf(
                createPackageAccessEntity("utdanning", "elev", true),
                createPackageAccessEntity("utdanning", "timeplan", true)
            )
        )
        Mockito.`when`(metamodelRepository.packages).thenReturn(setOf(elevPackage, timeplanPackage))

        val result = packageAccessService.getPackageAccess(clientName)

        Assertions.assertEquals(2, result.size)
        result.forEach(Consumer { access: PackageAccess -> Assertions.assertEquals(Status.ENABLED, access.status) })
        Mockito.verify(clientEntityRepository).findById(clientName)
        Mockito.verify(packageAccessEntityRepository).findByClient(clientEntity)
        Mockito.verify(metamodelRepository).packages
    }

    @Test
    fun packageAccessWithPartialAccess() {
        val clientName = "test-client"
        val clientEntity = ClientEntity()
        Mockito.`when`(clientEntityRepository.findById(clientName)).thenReturn(Optional.of(clientEntity))
        Mockito.`when`(packageAccessEntityRepository.findByClient(clientEntity))
            .thenReturn(listOf(createPackageAccessEntity("utdanning", "timeplan", false)))
        Mockito.`when`(metamodelRepository.packages).thenReturn(setOf(elevPackage, timeplanPackage))
        Mockito.`when`(resourceAccessEntityRepository.findByPackageId(ArgumentMatchers.any()))
            .thenReturn(listOf(createResourceAccessEntity(true)))

        val result = packageAccessService.getPackageAccess(clientName)

        Assertions.assertEquals(2, result.size)
        for ((_, packageName, status) in result) {
            if (packageName == "timeplan") {
                Assertions.assertEquals(
                    Status.PARTIAL,
                    status
                )
            } else {
                Assertions.assertEquals(
                    Status.DISABLED,
                    status
                )
            }
        }

        Mockito.verify(clientEntityRepository).findById(clientName)
        Mockito.verify(packageAccessEntityRepository).findByClient(clientEntity)
        Mockito.verify(metamodelRepository).packages
        Mockito.verify(resourceAccessEntityRepository)
            .findByPackageId(ArgumentMatchers.any())
    }

    private fun createPackageAccessEntity(
        domainName: String,
        packageName: String,
        hasFullAccess: Boolean
    ): PackageAccessEntity {
        return PackageAccessEntity(
            id = 1,
            domainName = domainName,
            packageName = packageName,
            hasFullaccess = hasFullAccess,
            client = ClientEntity()
        )
    }

    private fun createResourceAccessEntity(hasAccess: Boolean): ResourceAccessEntity {
        return ResourceAccessEntity(hasAccess = hasAccess)
    }
}
