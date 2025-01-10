package no.fintlabs.access.control.service;

import lombok.RequiredArgsConstructor;
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
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@RequiredArgsConstructor
public class AccessService {

    private final ClientEntityRepository clientEntityRepository;

    private final PackageAccessEntityRepository packageAccessEntityRepository;

    private final ResourceAccessEntityRepository resourceAccessEntityRepository;

    private final MetamodelRepository metamodelRepository;

    public Collection<PackageAccess> getPackageAccess(String clientOrAdapterName) {

        Optional<ClientEntity> clientEntity = clientEntityRepository.findById(clientOrAdapterName);
        Collection<PackageAccessEntity> entities = clientEntity.isEmpty() ? Collections.emptyList() : packageAccessEntityRepository.findByClient(clientEntity.get());

        List<PackageAccess> result = new ArrayList<>();

        for (Package aPackage : metamodelRepository.getPackages()) {

            Optional<PackageAccessEntity> packageAccessEntity = findPackageAccessEntity(aPackage, entities);
            Status status = Status.DISABLED;

            if (packageAccessEntity.isPresent()) {
                if (packageAccessEntity.get().getHasFullaccess()) {
                    status = Status.ENABLED;
                } else if (hasAnyPackageAccess(packageAccessEntity)) {
                    status = Status.PARTIAL;
                }
            }

            PackageAccess dto = new PackageAccess(aPackage.domainName(), aPackage.packageName(), status);
            result.add(dto);
        }

        return result;
    }

    private Optional<PackageAccessEntity> findPackageAccessEntity(Package aPackage, Collection<PackageAccessEntity> entities) {
        return entities.stream()
                .filter(entity -> entity.getDomainName().equals(aPackage.domainName()) && entity.getPackageName().equals(aPackage.packageName()))
                .findFirst();
    }

    private boolean hasAnyPackageAccess(Optional<PackageAccessEntity> packageAccessEntity) {
        Collection<ResourceAccessEntity> resources = resourceAccessEntityRepository.findByPackageId(packageAccessEntity.get().getId());
        return resources.stream().anyMatch(ResourceAccessEntity::getHasAccess);
    }
}
