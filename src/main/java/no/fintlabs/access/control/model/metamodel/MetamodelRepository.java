package no.fintlabs.access.control.model.metamodel;

import lombok.Getter;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Repository;
import org.springframework.web.client.RestClient;

import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;


// TODO: Should be named gateway instead of repository
@Repository
public class MetamodelRepository {

    private final RestClient restClient;

    @Getter
    private final List<Metamodel> metamodels;

    public MetamodelRepository(RestClient restClient) {
        this.restClient = restClient;
        this.metamodels = fetchMetamodels();
    }

    public Set<Package> getPackages() {
        return metamodels.stream()
                .map(model -> new Package(model.domainName(), model.packageName()))
                .collect(Collectors.toSet());
    }

    private List<Metamodel> fetchMetamodels() {
        return restClient.get()
                .retrieve()
                .toEntity(new ParameterizedTypeReference<List<Metamodel>>() {
                })
                .getBody();
    }

    public Collection<Metamodel> getResourceAccessByComponent(String componentName) {
        return metamodels.stream()
                .filter(metamodel -> componentName.equals(metamodel.domainName() + "_" + metamodel.packageName()))
                .toList();
    }
}
