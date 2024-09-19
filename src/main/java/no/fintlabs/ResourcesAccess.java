package no.fintlabs;

import java.util.List;

public record ResourcesAccess(
        String name,
        List<FieldAccess> fields,
        Multiplicity multiplicity,
        boolean enabled,
        boolean isWriteable
) {
}
