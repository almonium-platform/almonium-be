package com.almonium.auth.token.util

import jakarta.servlet.http.HttpServletRequest
import org.springframework.http.HttpHeaders
import org.springframework.util.StringUtils

object BearerTokenUtil {
    private const val BEARER_TOKEN_PREFIX = "Bearer "

    @JvmStatic
    fun bearerOf(token: String?): String? {
        return token?.let { "$BEARER_TOKEN_PREFIX$it" }
    }

    @JvmStatic
    fun getBearerTokenFromRequest(request: HttpServletRequest): String? {
        val bearerToken = request.getHeader(HttpHeaders.AUTHORIZATION)
        if (StringUtils.hasText(bearerToken) &&
            bearerToken.startsWith(
                BEARER_TOKEN_PREFIX,
                ignoreCase = true,
            )
        ) {
            return bearerToken.substring(BEARER_TOKEN_PREFIX.length)
        }
        return null
    }
}
