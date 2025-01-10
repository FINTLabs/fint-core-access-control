package no.fintlabs.access.control.controller;

import lombok.RequiredArgsConstructor;
import no.fintlabs.access.control.AccessCache;
import no.fintlabs.access.control.service.PackageAccessService;
import no.fintlabs.access.control.model.dto.FieldAccess;
import no.fintlabs.access.control.model.dto.PackageAccess;
import no.fintlabs.access.control.model.dto.ResourceAccess;
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

    private final PackageAccessService packageAccessService;

    @GetMapping
    public Collection<PackageAccess> getPackageAccess(@PathVariable String clientOrAdapterName) {
        return packageAccessService.getPackageAccess(clientOrAdapterName);
    }

    @GetMapping("/{component}")
    public Collection<ResourceAccess> getResourceAccess(@PathVariable String clientOrAdapterName, @PathVariable String component) {
        return accessCache.getResourceAccessByComponent(component);
    }

    @GetMapping("/{component}/{resource}")
    public Collection<FieldAccess> getFieldAccess(@PathVariable String clientOrAdapterName, @PathVariable String component, @PathVariable String resource) {
        return accessCache.getFieldAccessByComponentAndResource(component, resource);
    }

}
