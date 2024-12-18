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
@RequestMapping("/access/{clientOrAdapterName}")
@RequiredArgsConstructor
public class AccessController {

    private final AccessCache accessCache;

    private final AccessService accessService;

    @GetMapping
    public Collection<PackageAccess> getPackageAccess(@PathVariable String clientOrAdapterName) {
        return accessService.getPackageAccess(clientOrAdapterName);
    }

    @GetMapping("/{component}")
    public Collection<ResourcesAccess> getResourceAccess(@PathVariable String clientOrAdapterName, @PathVariable String component) {
        return accessCache.getResourceAccessByComponent(component);
    }

    @GetMapping("/{component}/{resource}")
    public Collection<FieldAccess> getFieldAccess(@PathVariable String clientOrAdapterName, @PathVariable String component, @PathVariable String resource) {
        return accessCache.getFieldAccessByComponentAndResource(component, resource);
    }

}
