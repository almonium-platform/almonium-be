package com.almonium.learning.rights.service;

import com.maxmind.db.Reader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.time.Clock;
import java.time.Duration;
import java.time.YearMonth;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;
import java.util.zip.GZIPInputStream;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.http.client.JdkClientHttpRequestFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientException;

/**
 * Which country an IP address is in, from DB-IP's free IP-to-Country Lite database (CC BY 4.0: the site credits
 * DB-IP). The database is downloaded at startup and once a month, about 8 MB, and held in memory; until it arrives,
 * or if it never does, every address is unknown and the caller falls back to its default country.
 */
@Slf4j
@Component
public class GeoIpCountries {
    private final RestClient restClient;
    private final Clock clock;
    private final boolean enabled;
    private final String urlTemplate;

    private volatile Reader reader;

    public GeoIpCountries(
            Clock clock,
            @Value("${app.rights.geoip.enabled:true}") boolean enabled,
            @Value("${app.rights.geoip.url-template:https://download.db-ip.com/free/dbip-country-lite-%s.mmdb.gz}")
                    String urlTemplate) {
        JdkClientHttpRequestFactory factory = new JdkClientHttpRequestFactory();
        factory.setReadTimeout(Duration.ofMinutes(2));
        this.restClient = RestClient.builder().requestFactory(factory).build();
        this.clock = clock;
        this.enabled = enabled;
        this.urlTemplate = urlTemplate;
    }

    /** The ISO 3166 alpha-2 code for an address, upper case; empty for private, malformed or unplaced addresses. */
    public Optional<String> country(String address) {
        Reader current = reader;
        if (current == null || address == null || address.isBlank()) {
            return Optional.empty();
        }
        try {
            InetAddress inet = InetAddress.ofLiteral(address.trim());
            if (inet.isLoopbackAddress() || inet.isSiteLocalAddress() || inet.isLinkLocalAddress()) {
                return Optional.empty();
            }
            Map<?, ?> record = current.get(inet, Map.class);
            if (record != null
                    && record.get("country") instanceof Map<?, ?> country
                    && country.get("iso_code") instanceof String code) {
                return Optional.of(code.toUpperCase(Locale.ROOT));
            }
            return Optional.empty();
        } catch (IllegalArgumentException | UnknownHostException exception) {
            return Optional.empty();
        } catch (IOException exception) {
            log.warn("IP country lookup failed for {}: {}", address, exception.getMessage());
            return Optional.empty();
        }
    }

    public boolean isLoaded() {
        return reader != null;
    }

    @EventListener(ApplicationReadyEvent.class)
    public void loadAtStartup() {
        if (enabled) {
            Thread.ofVirtual().name("geoip-load").start(this::refresh);
        }
    }

    /** DB-IP publishes on the first of the month; the third leaves room for a late release. */
    @Scheduled(cron = "0 0 5 3 * ?", zone = "UTC")
    public void refresh() {
        if (!enabled) {
            return;
        }
        YearMonth month = YearMonth.now(clock);
        for (YearMonth candidate : new YearMonth[] {month, month.minusMonths(1)}) {
            String url = urlTemplate.formatted(candidate);
            try {
                byte[] compressed = restClient.get().uri(url).retrieve().body(byte[].class);
                if (compressed == null) {
                    continue;
                }
                try (InputStream in = new GZIPInputStream(new ByteArrayInputStream(compressed))) {
                    reader = new Reader(in);
                }
                log.info("IP country database loaded from {}", url);
                return;
            } catch (RestClientException | IOException exception) {
                log.warn("IP country database not loaded from {}: {}", url, exception.getMessage());
            }
        }
    }

    /** For tests: use a database already in hand. */
    void use(Reader database) {
        this.reader = database;
    }
}
