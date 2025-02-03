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
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.MockitoAnnotations
import java.util.*

internal class ResourceAccessServiceTest {

    @Mock
    lateinit var clientEntityRepository: ClientEntityRepository

    @Mock
    lateinit var resourceAccessEntityRepository: ResourceAccessEntityRepository

    @Mock
    lateinit var packageAccessEntityRepository: PackageAccessEntityRepository

    @Mock
    lateinit var metamodelRepository: MetamodelRepository

    @Mock
    lateinit var fieldAccessService: FieldAccessService

    lateinit var resourceAccessService: ResourceAccessService

    @BeforeEach
    fun setUp() {
        MockitoAnnotations.openMocks(this)
        resourceAccessService = ResourceAccessService(
            clientEntityRepository,
            resourceAccessEntityRepository,
            packageAccessEntityRepository,
            metamodelRepository,
            fieldAccessService
        )
    }

    @Test
    fun resourceAccessWithNoClient() {
        val clientName = "test-client"
        val component = "test-component"

        Mockito.`when`(clientEntityRepository.findById(clientName)).thenReturn(Optional.empty())

        val result = resourceAccessService.getResourceAccess(clientName, component)

        Assertions.assertEquals(0, result.size)
        Mockito.verify(clientEntityRepository).findById(clientName)
        Mockito.verify(metamodelRepository).getResourceAccessByComponent(component)
        Mockito.verifyNoInteractions(
            resourceAccessEntityRepository,
            packageAccessEntityRepository,
            fieldAccessService
        )
    }

    @Test
    fun resourceAccessWithClientAndNoPackage() {
        val clientName = "test-client"
        val component = "test-component"
        val clientEntity = ClientEntity()

        Mockito.`when`(clientEntityRepository.findById(clientName)).thenReturn(Optional.of(clientEntity))
        Mockito.`when`(packageAccessEntityRepository.findByClient(clientEntity)).thenReturn(emptyList())

        val result = resourceAccessService.getResourceAccess(clientName, component)

        Assertions.assertEquals(0, result.size)
        Mockito.verify(metamodelRepository).getResourceAccessByComponent(component)
        Mockito.verify(clientEntityRepository).findById(clientName)
        Mockito.verify(packageAccessEntityRepository).findByClient(clientEntity)
        Mockito.verifyNoInteractions(resourceAccessEntityRepository, fieldAccessService)
    }

    @Test
    fun resourceAccessWithValidData() {
        val clientName = "test-client"
        val component = "test-component"
        val clientEntity = ClientEntity()

        val packageAccessEntity = PackageAccessEntity(
            id = 123,
            domainName = "test",
            packageName = "component",
            client = ClientEntity()
        )

        val resourceAccessEntity = ResourceAccessEntity(id = 321, resourceName = "elevforhold")

        val metamodel =
            Metamodel(
                "test",
                "component",
                "elevforhold",
                listOf("field1", "field2"),
                true
            )

        val resourceAccess = ResourceAccess(
            "elevforhold",
            listOf(),
            ReadingOption.SINGULAR,
            false,
            false
        )

        Mockito.`when`(clientEntityRepository.findById(clientName)).thenReturn(Optional.of(clientEntity))
        Mockito.`when`(packageAccessEntityRepository.findByClient(clientEntity)).thenReturn(listOf(packageAccessEntity))
        Mockito.`when`(resourceAccessEntityRepository.findByPackageId(packageAccessEntity.id ?: 0))
            .thenReturn(listOf(resourceAccessEntity))
        Mockito.`when`(metamodelRepository.getResourceAccessByComponent(component)).thenReturn(listOf(metamodel))
        Mockito.`when`(fieldAccessService.getFieldAccess(metamodel, resourceAccessEntity))
            .thenReturn(resourceAccess.fields)

        val result = resourceAccessService.getResourceAccess(clientName, component)

        Assertions.assertEquals(1, result.size)
        val resultAccess = result.iterator().next()
        Assertions.assertEquals("elevforhold", resultAccess.name)
        Assertions.assertEquals(ReadingOption.SINGULAR, resultAccess.readingOption)

        Mockito.verify(clientEntityRepository).findById(clientName)
        Mockito.verify(packageAccessEntityRepository).findByClient(clientEntity)
        //verify(resourceAccessEntityRepository).findByPackageId(packageAccessEntity.getId());
        Mockito.verify(metamodelRepository).getResourceAccessByComponent(component)
        Mockito.verify(fieldAccessService).getFieldAccess(metamodel, resourceAccessEntity)
    }
}
