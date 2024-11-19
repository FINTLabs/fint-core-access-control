package no.fintlabs.access.control.model;

public record PackageAccess(
        String domain,
        String packageName,
        Status status
) {
}
