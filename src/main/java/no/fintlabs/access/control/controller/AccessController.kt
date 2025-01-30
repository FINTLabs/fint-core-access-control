package no.fintlabs.access.control.controller

import lombok.RequiredArgsConstructor
import no.fintlabs.access.control.model.dto.FieldAccess
import no.fintlabs.access.control.model.dto.PackageAccess
import no.fintlabs.access.control.model.dto.ResourceAccess
import no.fintlabs.access.control.service.PackageAccessService
import no.fintlabs.access.control.service.ResourceAccessService
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/access/{clientOrAdapterName}")
@RequiredArgsConstructor
class AccessController(
    val packageAccessService: PackageAccessService,
    val resourceAccessService: ResourceAccessService
) {

    @GetMapping
    fun getPackageAccess(@PathVariable clientOrAdapterName: String): Collection<PackageAccess> {
        return packageAccessService.getPackageAccess(clientOrAdapterName)
    }

    @GetMapping("/{component}")
    fun getResourceAccess(
        @PathVariable clientOrAdapterName: String,
        @PathVariable component: String
    ): Collection<ResourceAccess> {
        return resourceAccessService.getResourceAccess(clientOrAdapterName, component)
    }

    @GetMapping("/{component}/{resource}")
    fun getFieldAccess(
        @PathVariable clientOrAdapterName: String,
        @PathVariable component: String,
        @PathVariable resource: String
    ): Collection<FieldAccess> {
        return resourceAccessService.getResourceAccess(clientOrAdapterName, component)
            .firstOrNull { it.name == resource }
            ?.fields
            ?: throw NoSuchElementException("ResourceAccess not found for resource: $resource")
    }
}
