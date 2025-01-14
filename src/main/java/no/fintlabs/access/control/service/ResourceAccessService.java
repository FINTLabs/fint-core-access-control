package no.fintlabs.access.control.service;

import no.fintlabs.access.control.model.dto.FieldAccess;
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
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class ResourceAccessService {

    private final ClientEntityRepository clientEntityRepository;
    private final ResourceAccessEntityRepository resourceAccessEntityRepository;
    private final PackageAccessEntityRepository packageAccessEntityRepository;
    private final MetamodelRepository metamodelRepository;
    private final FieldAccessService fieldAccessService;

    public ResourceAccessService(ClientEntityRepository clientEntityRepository, ResourceAccessEntityRepository resourceAccessEntityRepository, PackageAccessEntityRepository packageAccessEntityRepository, MetamodelRepository metamodelRepository, FieldAccessService fieldAccessService) {
        this.clientEntityRepository = clientEntityRepository;
        this.resourceAccessEntityRepository = resourceAccessEntityRepository;
        this.packageAccessEntityRepository = packageAccessEntityRepository;
        this.metamodelRepository = metamodelRepository;
        this.fieldAccessService = fieldAccessService;
    }

    public Collection<ResourceAccess> getResourceAccess(String clientOrAdapterName, String component) {
        Optional<ClientEntity> client = clientEntityRepository.findById(clientOrAdapterName);
        Collection<PackageAccessEntity> packageAccessEntities = client.isEmpty() ? Collections.emptyList() : packageAccessEntityRepository.findByClient(client.get());

        Optional<PackageAccessEntity> packageAccessEntity = getAccessEntity(component, packageAccessEntities);

        Collection<ResourceAccessEntity> resourceAccessEntities = packageAccessEntity.isEmpty() ? Collections.emptyList() : resourceAccessEntityRepository.findByPackageId(packageAccessEntity.get().getId());

        List<ResourceAccess> result = new ArrayList<>();
        for (Metamodel metamodel : metamodelRepository.getResourceAccessByComponent(component)) {
            Optional<ResourceAccessEntity> resourceAccessEntity = resourceAccessEntities.stream()
                    .filter(entity -> entity.getResourceName().equals(metamodel.resourceName()))
                    .findFirst();

            String resourceName = metamodel.resourceName();
            List<FieldAccess> fieldAccesses = fieldAccessService.getFieldAccess(metamodel, resourceAccessEntity);
            ReadingOption readingOption = resourceAccessEntity.isPresent() && resourceAccessEntity.get().getHasReadAllAccess() ? ReadingOption.MULTIPLE : ReadingOption.SINGULAR;
            boolean enabled = (resourceAccessEntity.isPresent() && resourceAccessEntity.get().getHasAccess()) || (packageAccessEntity.isPresent() && packageAccessEntity.get().getHasFullaccess());
            boolean isWriteable = (packageAccessEntity.isPresent() && packageAccessEntity.get().getHasFullaccess()) || (resourceAccessEntity.isPresent() && resourceAccessEntity.get().getHasWriteAccess());

            ResourceAccess resourceAccess = new ResourceAccess(resourceName, fieldAccesses, readingOption, enabled, isWriteable);
            result.add(resourceAccess);
        }

        return result;
    }

    private Optional<PackageAccessEntity> getAccessEntity(String component, Collection<PackageAccessEntity> packageAccessEntities) {
        for (PackageAccessEntity packageAccessEntity : packageAccessEntities) {
            if (component.equals(packageAccessEntity.getDomainName() + "_" + packageAccessEntity.getPackageName())) {
                return Optional.of(packageAccessEntity);
            }
        }
        return Optional.empty();
    }

}
