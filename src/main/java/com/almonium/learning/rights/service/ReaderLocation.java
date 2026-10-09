package com.almonium.learning.rights.service;

import jakarta.servlet.http.HttpServletRequest;
import java.util.Locale;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

/**
 * The country the current request comes from, by its address. Traefik is the only way in and the application trusts
 * its forwarded headers, so {@code getRemoteAddr()} is the reader's address. An address that cannot be placed (a
 * private network, the database not loaded yet, a request outside the web layer) counts as the fallback country,
 * Germany by default: the home market, and the strictest rules the shelf already passed.
 */
@Component
public class ReaderLocation {
    private static final String ATTRIBUTE = ReaderLocation.class.getName() + ".country";

    private final GeoIpCountries geoIpCountries;
    private final String fallback;

    public ReaderLocation(GeoIpCountries geoIpCountries, @Value("${app.rights.unknown-country:DE}") String fallback) {
        this.geoIpCountries = geoIpCountries;
        this.fallback = fallback.toUpperCase(Locale.ROOT);
    }

    public String country() {
        if (!(RequestContextHolder.getRequestAttributes() instanceof ServletRequestAttributes attributes)) {
            return fallback;
        }
        HttpServletRequest request = attributes.getRequest();
        if (request.getAttribute(ATTRIBUTE) instanceof String cached) {
            return cached;
        }
        String country = geoIpCountries.country(request.getRemoteAddr()).orElse(fallback);
        request.setAttribute(ATTRIBUTE, country);
        return country;
    }

    /** The country as the address places it, without the fallback; empty when it cannot be placed. */
    public Optional<String> detectedCountry() {
        if (!(RequestContextHolder.getRequestAttributes() instanceof ServletRequestAttributes attributes)) {
            return Optional.empty();
        }
        return geoIpCountries.country(attributes.getRequest().getRemoteAddr());
    }
}
