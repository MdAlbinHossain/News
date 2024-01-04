package bd.com.albin.news.data.network.models

import bd.com.albin.news.data.local.entities.Source
import kotlinx.serialization.Serializable

@Serializable
data class NetworkSource(
    val id: String?,
    val name: String,
)

fun NetworkSource.asLocal() = Source(id = id, name = name)
fun NetworkSource.asExternal() = Source(id = id, name = name)