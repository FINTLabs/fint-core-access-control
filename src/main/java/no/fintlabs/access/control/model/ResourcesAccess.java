package no.fintlabs.access.control.model;

import java.util.List;

public record ResourcesAccess(
        String name,
        List<FieldAccess> fields,
        Multiplicity multiplicity,
        boolean enabled,
        boolean isWriteable
) {
}
