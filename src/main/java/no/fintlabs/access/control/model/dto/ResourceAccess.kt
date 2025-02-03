package no.fintlabs.access.control.model.dto

data class ResourceAccess(
    val name: String,
    val fields: List<FieldAccess>,
    val readingOption: ReadingOption,
    val enabled: Boolean,
    val isWriteable: Boolean
)
