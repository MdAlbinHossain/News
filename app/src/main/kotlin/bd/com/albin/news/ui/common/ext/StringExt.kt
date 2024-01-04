package bd.com.albin.news.ui.common.ext

import java.util.Base64

fun String.encodeUrl(): String {
    return Base64.getUrlEncoder().encodeToString(this.toByteArray())
}

fun String.decodeUrl(): String {
    return String(Base64.getUrlDecoder().decode(this))
}

fun String.ensureHttpsUrl(): String {
    return if (this.startsWith("http://")) {
        this.replaceFirst("http://", "https://")
    } else {
        this
    }
}