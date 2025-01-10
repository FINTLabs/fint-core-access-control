package no.fintlabs.access.control.service;

import no.fintlabs.access.control.model.dto.PackageAccess;
import no.fintlabs.access.control.model.dto.Status;
import no.fintlabs.access.control.model.entity.ClientEntity;
import no.fintlabs.access.control.model.entity.PackageAccessEntity;
import no.fintlabs.access.control.model.entity.ResourceAccessEntity;
import no.fintlabs.access.control.model.metamodel.MetamodelRepository;
import no.fintlabs.access.control.model.metamodel.Package;
import no.fintlabs.access.control.repository.ClientEntityRepository;
import no.fintlabs.access.control.repository.PackageAccessEntityRepository;
import no.fintlabs.access.control.repository.ResourceAccessEntityRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class PackageAccessServiceTest {

    private final Package elevPackage = new Package("utdanning", "elev");
    private final Package timeplanPackage = new Package("utdanning", "timeplan");

    @Mock
    private ClientEntityRepository clientEntityRepository;

    @Mock
    private PackageAccessEntityRepository packageAccessEntityRepository;

    @Mock
    private ResourceAccessEntityRepository resourceAccessEntityRepository;

    @Mock
    private MetamodelRepository metamodelRepository;

    private PackageAccessService packageAccessService;


    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        packageAccessService = new PackageAccessService(
                clientEntityRepository,
                packageAccessEntityRepository,
                resourceAccessEntityRepository,
                metamodelRepository
        );
    }

    @Test
    void getPackageAccessWithNoAccess() {
        String clientName = "test-client@fintlabs.no";
        when(clientEntityRepository.findById(clientName)).thenReturn(Optional.empty());
        when(metamodelRepository.getPackages()).thenReturn(Set.of(elevPackage, timeplanPackage));

        Collection<PackageAccess> result = packageAccessService.getPackageAccess(clientName);

        assertEquals(2, result.size());
        result.forEach(access -> assertEquals(Status.DISABLED, access.status()));
        verify(clientEntityRepository).findById(clientName);
        verify(metamodelRepository).getPackages();
        verifyNoInteractions(packageAccessEntityRepository, resourceAccessEntityRepository);
    }

    @Test
    void getPackageAccessWithFullAccess() {
        String clientName = "test-client";
        ClientEntity clientEntity = new ClientEntity();
        when(clientEntityRepository.findById(clientName)).thenReturn(Optional.of(clientEntity));
        when(packageAccessEntityRepository.findByClient(clientEntity)).thenReturn(List.of(
                createPackageAccessEntity("utdanning", "elev", true),
                createPackageAccessEntity("utdanning", "timeplan", true)));
        when(metamodelRepository.getPackages()).thenReturn(Set.of(elevPackage, timeplanPackage));

        Collection<PackageAccess> result = packageAccessService.getPackageAccess(clientName);

        assertEquals(2, result.size());
        result.forEach(access -> assertEquals(Status.ENABLED, access.status()));
        verify(clientEntityRepository).findById(clientName);
        verify(packageAccessEntityRepository).findByClient(clientEntity);
        verify(metamodelRepository).getPackages();
    }

    @Test
    void getPackageAccessWithPartialAccess() {
        String clientName = "test-client";
        ClientEntity clientEntity = new ClientEntity();
        when(clientEntityRepository.findById(clientName)).thenReturn(Optional.of(clientEntity));
        when(packageAccessEntityRepository.findByClient(clientEntity)).thenReturn(List.of(createPackageAccessEntity("utdanning", "timeplan", false)));
        when(metamodelRepository.getPackages()).thenReturn(Set.of(elevPackage, timeplanPackage));
        when(resourceAccessEntityRepository.findByPackageId(any())).thenReturn(List.of(createResourceAccessEntity(true)));

        Collection<PackageAccess> result = packageAccessService.getPackageAccess(clientName);

        assertEquals(2, result.size());
        for (PackageAccess access : result) {
            if (access.packageName().equals("timeplan")) {
                assertEquals(Status.PARTIAL, access.status());
            } else {
                assertEquals(Status.DISABLED, access.status());
            }
        }

        verify(clientEntityRepository).findById(clientName);
        verify(packageAccessEntityRepository).findByClient(clientEntity);
        verify(metamodelRepository).getPackages();
        verify(resourceAccessEntityRepository).findByPackageId(any());
    }

    private PackageAccessEntity createPackageAccessEntity(String domainName, String packageName, boolean hasFullAccess) {
        PackageAccessEntity entity = new PackageAccessEntity();
        entity.setDomainName(domainName);
        entity.setPackageName(packageName);
        entity.setHasFullaccess(hasFullAccess);
        return entity;
    }

    private ResourceAccessEntity createResourceAccessEntity(boolean hasAccess) {
        ResourceAccessEntity resourceAccessEntity = new ResourceAccessEntity();
        resourceAccessEntity.setHasAccess(hasAccess);
        return resourceAccessEntity;
    }
}
