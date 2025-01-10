package no.fintlabs.access.control.model.dto;


import java.util.List;

public record ResourcesAccess(
        String name,
        List<FieldAccess> fields,
        ReadingOption readingOption,
        boolean enabled,
        boolean isWriteable
) {
}
