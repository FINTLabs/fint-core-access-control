package no.fintlabs.access.control.model.dto;

import java.util.List;

public record FieldAccess(
        String name,
        List<String> shouldContain,
        boolean isHidden
) {
}
