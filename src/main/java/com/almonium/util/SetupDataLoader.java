package com.almonium.util;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class SetupDataLoader {

    private boolean alreadySetup = false;

    @EventListener
    public void handleContextRefreshed(ContextRefreshedEvent event) {
        if (alreadySetup) {
            return;
        }
        log.info("Loading setup data...");
        // setup data

        alreadySetup = true;
    }
}
