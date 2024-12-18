package no.fintlabs.access.control;

import lombok.RequiredArgsConstructor;
import no.fintlabs.access.control.model.PackageAccess;
import no.fintlabs.access.control.model.entity.ClientEntity;
import no.fintlabs.access.control.model.entity.PackageAccessEntity;
import no.fintlabs.access.control.repository.ClientEntityRepository;
import no.fintlabs.access.control.repository.PackageAccessEntityRepository;
import org.springframework.stereotype.Service;

import java.util.Collection;

@Service
@RequiredArgsConstructor
public class AccessService {

    private final ClientEntityRepository clientEntityRepository;

    private final PackageAccessEntityRepository packageAccessEntityRepository;

    public Collection<PackageAccess> getPackageAccess(String clientOrAdapterName) {
        //TODO: handle new client
        ClientEntity clientEntity = clientEntityRepository.findById(clientOrAdapterName).orElseThrow();
        //TODO: handle new package, check metamodel
        Collection<PackageAccessEntity> entities = packageAccessEntityRepository.findByClient(clientEntity);
        return
    }
}
