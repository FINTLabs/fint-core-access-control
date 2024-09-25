package no.fintlabs.access.control;

import lombok.RequiredArgsConstructor;
import no.fintlabs.access.control.model.FieldAccess;
import no.fintlabs.access.control.model.PackageAccess;
import no.fintlabs.access.control.model.ResourcesAccess;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collection;

@RestController
@RequestMapping("/package/{organization}")
@RequiredArgsConstructor
public class AccessController {

    private final AccessCache accessCache;

    @GetMapping
    public Collection<PackageAccess> getPackageAccess(@PathVariable String organization) {
        return accessCache.getPackageAccesses();
    }

    @GetMapping("/{componentName}")
    public Collection<ResourcesAccess> getResourceAccess(@PathVariable String componentName, @PathVariable String organization) {
        return accessCache.getResourceAccessByComponent(componentName);
    }

    @GetMapping("/{componentName}/{resource}")
    public Collection<FieldAccess> getFieldAccess(@PathVariable String componentName, @PathVariable String resource, @PathVariable String organization) {
        return accessCache.getFieldAccessByComponentAndResource(componentName, resource);
    }


}
