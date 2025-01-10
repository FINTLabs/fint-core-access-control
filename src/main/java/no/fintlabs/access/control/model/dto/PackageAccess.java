package no.fintlabs.access.control.model.dto;

public record PackageAccess(
        String domain,
        String packageName,
        Status status
) {
}
