package no.fintlabs.access.control.service;

import no.fintlabs.access.control.model.dto.FieldAccess;
import no.fintlabs.access.control.model.entity.FieldAccessEntity;
import no.fintlabs.access.control.model.entity.ResourceAccessEntity;
import no.fintlabs.access.control.model.metamodel.Metamodel;
import no.fintlabs.access.control.repository.FieldAccessEntityRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class FieldAccessServiceTest {

    @Mock
    private FieldAccessEntityRepository fieldAccessEntityRepository;

    private FieldAccessService fieldAccessService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        fieldAccessService = new FieldAccessService(fieldAccessEntityRepository);
    }

    @Test
    void getFieldAccessWithEmptyResourceAccessEntity() {
        List<FieldAccess> result = fieldAccessService.getFieldAccess(getMetamodell(), Optional.empty());

        assertEquals(2, result.size());
        result.forEach(field -> {
            assertEquals(Collections.emptyList(), field.shouldContain());
            assertEquals(false, field.isHidden());
        });
        verifyNoInteractions(fieldAccessEntityRepository);
    }

    @Test
    void getFieldAccessWithExistingEntities() {
        Metamodel metamodel = getMetamodell();

        ResourceAccessEntity resourceAccessEntity = new ResourceAccessEntity();
        resourceAccessEntity.setId(1);

        FieldAccessEntity field1Entity = new FieldAccessEntity();
        field1Entity.setFieldName("field1");
        field1Entity.setMustContain("value1");
        field1Entity.setHasAccess(true);

        when(fieldAccessEntityRepository.findByResourceId(1)).thenReturn(List.of(field1Entity));

        List<FieldAccess> result = fieldAccessService.getFieldAccess(metamodel, Optional.of(resourceAccessEntity));

        assertEquals(2, result.size());

        FieldAccess field1 = result.get(0);
        assertEquals("field1", field1.name());
        assertEquals(List.of("value1"), field1.shouldContain());
        assertEquals(false, field1.isHidden());

        FieldAccess field2 = result.get(1);
        assertEquals("field2", field2.name());
        assertEquals(Collections.emptyList(), field2.shouldContain());
        assertEquals(false, field2.isHidden());

        verify(fieldAccessEntityRepository).findByResourceId(1);
    }

    private Metamodel getMetamodell() {
        return new Metamodel(
                "utdanning",
                "elev",
                "elevforhold",
                List.of("field1", "field2"),
                true
        );
    }
}


