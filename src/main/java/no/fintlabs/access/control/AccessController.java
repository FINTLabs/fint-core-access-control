package no.fintlabs.access.control;

import no.fintlabs.access.control.model.PackageAccess;
import no.fintlabs.access.control.model.ResourcesAccess;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collection;

@RestController
@RequestMapping("/package")
public class AccessController {

    @GetMapping
    public Collection<PackageAccess> getPackageAccess() {
        return null;
    }

    @GetMapping("/{packageName}/resource")
    public Collection<ResourcesAccess> getResourceAccess(@PathVariable String packageName) {
        return null;
    }

    @GetMapping("/{packageName}/{resource}/field")
    public Collection<ResourcesAccess> getFieldAccess(@PathVariable String packageName, @PathVariable String resource) {
        return null;
    }


}
