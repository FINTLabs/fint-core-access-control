package no.fintlabs.access.control.model.metamodel

@JvmRecord
data class Metamodel(
    @JvmField val domainName: String,
    @JvmField val packageName: String,
    @JvmField val resourceName: String,
    @JvmField val fields: List<String>,
    val writeable: Boolean
)
