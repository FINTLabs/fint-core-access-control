package no.fintlabs.access.control.model.dto;


import java.util.List;

public record ResourceAccess(
        String name,
        List<FieldAccess> fields,
        ReadingOption readingOption,
        boolean enabled,
        boolean isWriteable
) {
}
