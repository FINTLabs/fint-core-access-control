package no.fintlabs.access.control;

import lombok.RequiredArgsConstructor;
import no.fintlabs.access.control.model.dto.PackageAccess;
import no.fintlabs.access.control.model.entity.ClientEntity;
import no.fintlabs.access.control.model.entity.PackageAccessEntity;
import no.fintlabs.access.control.model.metamodel.MetamodelRepository;
import no.fintlabs.access.control.repository.ClientEntityRepository;
import no.fintlabs.access.control.repository.PackageAccessEntityRepository;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Collections;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AccessService {

    private final ClientEntityRepository clientEntityRepository;

    private final PackageAccessEntityRepository packageAccessEntityRepository;

    private final MetamodelRepository metamodelRepository;

    public Collection<PackageAccess> getPackageAccess(String clientOrAdapterName) {

        Optional<ClientEntity> clientEntity = clientEntityRepository.findById(clientOrAdapterName);
        Collection<PackageAccessEntity> entities = clientEntity.isEmpty() ? Collections.emptyList() : packageAccessEntityRepository.findByClient(clientEntity.get());


        return null;
    }

}
