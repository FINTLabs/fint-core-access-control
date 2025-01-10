package no.fintlabs.access.control;

import lombok.Getter;
import no.fintlabs.access.control.model.dto.*;
import no.fintlabs.access.control.model.metamodel.Metamodel;
import no.fintlabs.access.control.model.metamodel.MetamodelRepository;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
public class AccessCache {

    private final MetamodelRepository metamodelRepository;

    @Getter
    private final Set<PackageAccess> packageAccesses = new HashSet<>();
    private final Map<String, Map<String, ResourceAccess>> resourceAccessMap = new HashMap<>();

    public AccessCache(MetamodelRepository metamodelRepository) {
        this.metamodelRepository = metamodelRepository;
        initializeCache();
    }

    public Collection<ResourceAccess> getResourceAccessByComponent(String componentName) {
        return resourceAccessMap.getOrDefault(componentName, new HashMap<>()).values();
    }

    public Collection<FieldAccess> getFieldAccessByComponentAndResource(String componentName, String resource) {
        return resourceAccessMap.getOrDefault(componentName, new HashMap<>()).get(resource).fields();
    }

    private void initializeCache() {
        metamodelRepository.getMetamodels().forEach(metamodel -> {
            packageAccesses.add(new PackageAccess(metamodel.domainName(), metamodel.packageName(), Status.DISABLED));

            resourceAccessMap.computeIfAbsent(
                    createComponentName(metamodel),
                    k -> new HashMap<>()
            ).put(metamodel.resourceName(), createResourceAccess(metamodel));
        });
    }

    private ResourceAccess createResourceAccess(Metamodel metamodel) {
        List<FieldAccess> fieldAccesses = new ArrayList<>();
        metamodel.fields().forEach(field -> fieldAccesses.add(new FieldAccess(field, new ArrayList<>(), false)));
        return new ResourceAccess(metamodel.resourceName(), fieldAccesses, ReadingOption.MULTIPLE, false, metamodel.writeable());
    }

    private String createComponentName(Metamodel metamodel) {
        return "%s_%s".formatted(metamodel.domainName(), metamodel.packageName());
    }
}
