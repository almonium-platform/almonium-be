package com.almonium.util.http

object BearerTokenUtil {
    @JvmStatic
    fun bearerOf(token: String): String = "Bearer $token"
}
