package com.edgelab.marketdata.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.endpoint.event.RefreshEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class ConsulEventListener {

    @EventListener
    public void handle(RefreshEvent event) {
            log.info("Event getEventDesc " + event.getEventDesc());
            log.info("Event getEvent: " + event.getEvent());
    }

}
