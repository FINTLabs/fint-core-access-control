package no.fintlabs.access.control.model.dto

data class PackageAccess(
    val domain: String,
    val packageName: String,
    val status: Status
)
