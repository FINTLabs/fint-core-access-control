package no.fintlabs.access.control;

import java.util.List;

public record Metamodel(
        String domainName,
        String packageName,
        String resourceName,
        List<String> fields,
        boolean writeable
) {
}
