package com.edgelab.marketdata.config;

import com.ecwid.consul.v1.ConsulClient;
import com.ecwid.consul.v1.QueryParams;
import com.ecwid.consul.v1.Response;
import com.ecwid.consul.v1.session.model.NewSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Component
@RequiredArgsConstructor
@Slf4j
public class LeaderConfig {

    private final ConsulClient consulClient;

    @Value("${spring.cloud.consul.config.prefix}")
    private String prefix;

    @Value("${spring.application.name}")
    private String application;

    @PostConstruct
    private void electLeader() {
        Response<String> session =  consulClient.sessionCreate(new NewSession(), QueryParams.DEFAULT);
        String sessionId = session.getValue();
        log.info("SESS ID: {}", sessionId);
    }

}
