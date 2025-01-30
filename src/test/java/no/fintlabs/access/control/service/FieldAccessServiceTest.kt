package no.fintlabs.access.control.service

import no.fintlabs.access.control.model.dto.FieldAccess
import no.fintlabs.access.control.model.entity.FieldAccessEntity
import no.fintlabs.access.control.model.entity.ResourceAccessEntity
import no.fintlabs.access.control.model.metamodel.Metamodel
import no.fintlabs.access.control.repository.FieldAccessEntityRepository
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.MockitoAnnotations
import java.util.List
import java.util.function.Consumer

internal class FieldAccessServiceTest(
    @Mock
    private val fieldAccessEntityRepository: FieldAccessEntityRepository,
    private var fieldAccessService: FieldAccessService
) {

    @BeforeEach
    fun setUp() {
        MockitoAnnotations.openMocks(this)
        fieldAccessService = FieldAccessService(fieldAccessEntityRepository)
    }

    @get:Test
    val fieldAccessWithEmptyResourceAccessEntity: Unit
        get() {
            val result = fieldAccessService!!.getFieldAccess(metamodell, null)

            Assertions.assertEquals(2, result.size)
            result.forEach(Consumer { field: FieldAccess ->
                Assertions.assertEquals(
                    emptyList<Any>(),
                    field.shouldContain
                )
                Assertions.assertEquals(false, field.isHidden)
            })
            Mockito.verifyNoInteractions(fieldAccessEntityRepository)
        }

    @get:Test
    val fieldAccessWithExistingEntities: Unit
        get() {
            val metamodel = metamodell

            val resourceAccessEntity = ResourceAccessEntity(id = 1)

            val field1Entity = FieldAccessEntity(
                fieldName = "field1",
                mustContain = "value1",
                hasAccess = true
            )

            Mockito.`when`(
                fieldAccessEntityRepository!!.findByResourceId(
                    1
                )
            ).thenReturn(List.of(field1Entity))

            val result =
                fieldAccessService.getFieldAccess(metamodel, resourceAccessEntity)

            Assertions.assertEquals(2, result.size)

            val field1 = result[0]
            Assertions.assertEquals("field1", field1.name)
            Assertions.assertEquals(
                listOf("value1"),
                field1.shouldContain
            )
            Assertions.assertEquals(false, field1.isHidden)

            val field2 = result[1]
            Assertions.assertEquals("field2", field2.name)
            Assertions.assertEquals(
                emptyList<Any>(),
                field2.shouldContain
            )
            Assertions.assertEquals(false, field2.isHidden)

            Mockito.verify(fieldAccessEntityRepository).findByResourceId(1)
        }

    private val metamodell: Metamodel
        get() = Metamodel(
            "utdanning",
            "elev",
            "elevforhold",
            listOf("field1", "field2"),
            true
        )
}


