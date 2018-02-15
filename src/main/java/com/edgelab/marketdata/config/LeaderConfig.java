package com.edgelab.marketdata.config;

import com.ecwid.consul.v1.ConsulClient;
import com.ecwid.consul.v1.QueryParams;
import com.ecwid.consul.v1.Response;
import com.ecwid.consul.v1.kv.model.GetValue;
import com.ecwid.consul.v1.kv.model.PutParams;
import com.ecwid.consul.v1.session.model.NewSession;
import com.google.common.collect.ImmutableList;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class LeaderConfig {

    private static final List<String> CHECKS  = ImmutableList.of("service:rx-smoker", "serfHealth");

    private final ConsulClient consulClient;

    public void electLeader() {
        NewSession newSession = new NewSession();
        newSession.setChecks(CHECKS);

        String sessionId = createSession(newSession);
        acquireLeadership(sessionId);
        checkCurrentLeader();
    }

    private String createSession(NewSession newSession) {
        Response<String> session =  consulClient.sessionCreate(newSession, QueryParams.DEFAULT);
        String sessionId = session.getValue();
        log.info("SESSION ID: {}", sessionId);
        return sessionId;
    }

    private void acquireLeadership(String sessionId) {
        Response<Boolean> response = consulClient.setKVValue("services/rx-smoker/leader", sessionId, new PutParams() {{
            setAcquireSession(sessionId);
        }});

        log.info("ACQUIRE LEADER LOCK: {}", response);
    }

    private void checkCurrentLeader() {
        Response<GetValue> valueResponse = consulClient.getKVValue("services/rx-smoker/leader");

        if (valueResponse.getValue() != null) {
            log.info("CURRENT LEADER: {}", valueResponse.getValue().getDecodedValue());
        }
    }

}
