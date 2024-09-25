package no.fintlabs.access.control;

import no.fintlabs.access.control.model.*;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class AccessCache {

    private final MetamodelRepository metamodelRepository;
    private final List<PackageAccess> packageAccesses = new ArrayList<>();
    private final Map<String, Map<String, ResourcesAccess>> resourceAccessMap = new HashMap<>();

    public AccessCache(MetamodelRepository metamodelRepository) {
        this.metamodelRepository = metamodelRepository;
        initializeCache();
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

    private ResourcesAccess createResourceAccess(Metamodel metamodel) {
        List<FieldAccess> fieldAccesses = new ArrayList<>();
        metamodel.fields().forEach(field -> fieldAccesses.add(new FieldAccess(field, new ArrayList<>(), false)));
        return new ResourcesAccess(metamodel.resourceName(), fieldAccesses, ReadingOption.MULTIPLE, false, metamodel.writeable());
    }

    private String createComponentName(Metamodel metamodel) {
        return "%s-%s".formatted(metamodel.domainName(), metamodel.packageName());
    }

}
