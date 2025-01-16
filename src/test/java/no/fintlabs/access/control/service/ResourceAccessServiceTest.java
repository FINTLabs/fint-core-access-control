package no.fintlabs.access.control.service;

import no.fintlabs.access.control.model.dto.ReadingOption;
import no.fintlabs.access.control.model.dto.ResourceAccess;
import no.fintlabs.access.control.model.entity.ClientEntity;
import no.fintlabs.access.control.model.entity.PackageAccessEntity;
import no.fintlabs.access.control.model.entity.ResourceAccessEntity;
import no.fintlabs.access.control.model.metamodel.Metamodel;
import no.fintlabs.access.control.model.metamodel.MetamodelRepository;
import no.fintlabs.access.control.repository.ClientEntityRepository;
import no.fintlabs.access.control.repository.PackageAccessEntityRepository;
import no.fintlabs.access.control.repository.ResourceAccessEntityRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class ResourceAccessServiceTest {

    @Mock
    private ClientEntityRepository clientEntityRepository;

    @Mock
    private ResourceAccessEntityRepository resourceAccessEntityRepository;

    @Mock
    private PackageAccessEntityRepository packageAccessEntityRepository;

    @Mock
    private MetamodelRepository metamodelRepository;

    @Mock
    private FieldAccessService fieldAccessService;

    private ResourceAccessService resourceAccessService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        resourceAccessService = new ResourceAccessService(
                clientEntityRepository,
                resourceAccessEntityRepository,
                packageAccessEntityRepository,
                metamodelRepository,
                fieldAccessService
        );
    }

    @Test
    void getResourceAccessWithNoClient() {
        String clientName = "test-client";
        String component = "test-component";

        when(clientEntityRepository.findById(clientName)).thenReturn(Optional.empty());

        Collection<ResourceAccess> result = resourceAccessService.getResourceAccess(clientName, component);

        assertEquals(0, result.size());
        verify(clientEntityRepository).findById(clientName);
        verify(metamodelRepository).getResourceAccessByComponent(component);
        verifyNoInteractions(resourceAccessEntityRepository, packageAccessEntityRepository, fieldAccessService);
    }

    @Test
    void getResourceAccessWithClientAndNoPackage() {
        String clientName = "test-client";
        String component = "test-component";
        ClientEntity clientEntity = new ClientEntity();

        when(clientEntityRepository.findById(clientName)).thenReturn(Optional.of(clientEntity));
        when(packageAccessEntityRepository.findByClient(clientEntity)).thenReturn(Collections.emptyList());

        Collection<ResourceAccess> result = resourceAccessService.getResourceAccess(clientName, component);

        assertEquals(0, result.size());
        verify(metamodelRepository).getResourceAccessByComponent(component);
        verify(clientEntityRepository).findById(clientName);
        verify(packageAccessEntityRepository).findByClient(clientEntity);
        verifyNoInteractions(resourceAccessEntityRepository, fieldAccessService);
    }

    @Test
    void getResourceAccessWithValidData() {
        String clientName = "test-client";
        String component = "test-component";
        ClientEntity clientEntity = new ClientEntity();

        PackageAccessEntity packageAccessEntity = new PackageAccessEntity();
        packageAccessEntity.setId(123);
        packageAccessEntity.setDomainName("test");
        packageAccessEntity.setPackageName("component");

        ResourceAccessEntity resourceAccessEntity = new ResourceAccessEntity();
        resourceAccessEntity.setId(321);
        resourceAccessEntity.setResourceName("elevforhold");

        Metamodel metamodel = new Metamodel(
                "test",
                "component",
                "elevforhold",
                List.of("field1", "field2"),
                true
        );

        ResourceAccess resourceAccess = new ResourceAccess("elevforhold", List.of(), ReadingOption.SINGULAR, false, false);

        when(clientEntityRepository.findById(clientName)).thenReturn(Optional.of(clientEntity));
        when(packageAccessEntityRepository.findByClient(clientEntity)).thenReturn(List.of(packageAccessEntity));
        when(resourceAccessEntityRepository.findByPackageId(packageAccessEntity.getId()))
                .thenReturn(List.of(resourceAccessEntity));
        when(metamodelRepository.getResourceAccessByComponent(component)).thenReturn(List.of(metamodel));
        when(fieldAccessService.getFieldAccess(metamodel, Optional.of(resourceAccessEntity))).thenReturn(resourceAccess.fields());

        Collection<ResourceAccess> result = resourceAccessService.getResourceAccess(clientName, component);

        assertEquals(1, result.size());
        ResourceAccess resultAccess = result.iterator().next();
        assertEquals("elevforhold", resultAccess.name());
        assertEquals(ReadingOption.SINGULAR, resultAccess.readingOption());

        verify(clientEntityRepository).findById(clientName);
        verify(packageAccessEntityRepository).findByClient(clientEntity);
        //verify(resourceAccessEntityRepository).findByPackageId(packageAccessEntity.getId());
        verify(metamodelRepository).getResourceAccessByComponent(component);
        verify(fieldAccessService).getFieldAccess(metamodel, Optional.of(resourceAccessEntity));
    }
}
