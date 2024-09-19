package no.fintlabs;

public record PackageAccess(
        String domain,
        String packageName,
        Status status
) {
}
