package com.linguatool.other;

import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.ZoneOffset;

@Component
public class ClockService {

    public LocalDateTime getCurrentDateTimeUtc() {
        return LocalDateTime.now(ZoneOffset.UTC);
    }

}
