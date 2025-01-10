package no.fintlabs.access.control.model.metamodel;

import java.util.List;

public record Metamodel(
        String domainName,
        String packageName,
        String resourceName,
        List<String> fields,
        boolean writeable
) {
}
