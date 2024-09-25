package no.fintlabs.access.control;

import lombok.Getter;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Repository;
import org.springframework.web.client.RestClient;

import java.util.List;


@Repository
public class MetamodelRepository {

    private final RestClient restClient;

    @Getter
    private final List<Metamodel> metamodels;

    public MetamodelRepository(RestClient restClient) {
        this.restClient = restClient;
        this.metamodels = fetchMetamodels();
    }

    private List<Metamodel> fetchMetamodels() {
        return restClient.get()
                .retrieve()
                .toEntity(new ParameterizedTypeReference<List<Metamodel>>() {
                })
                .getBody();
    }

}
