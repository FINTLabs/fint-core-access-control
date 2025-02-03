package no.fintlabs.access.control.model.dto

data class FieldAccess(
    val name: String,
    val shouldContain: List<String>,
    val isHidden: Boolean
)
