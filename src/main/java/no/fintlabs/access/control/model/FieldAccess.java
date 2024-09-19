package no.fintlabs.access.control.model;

import java.util.List;

public record FieldAccess(
        String name,
        List<String> shouldContain,
        boolean isHidden
) {
}
