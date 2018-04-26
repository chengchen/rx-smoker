package com.edgelab.marketdata.config;

import com.ecwid.consul.v1.ConsulClient;
import com.ecwid.consul.v1.QueryParams;
import com.ecwid.consul.v1.Response;
import com.ecwid.consul.v1.kv.model.GetValue;
import com.ecwid.consul.v1.kv.model.PutParams;
import com.ecwid.consul.v1.session.model.NewSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicLong;

@Service
@EnableConfigurationProperties(LeaderElectionConfig.class)
@RequiredArgsConstructor
@Slf4j
public class LeaderElectionService {

    private final ConsulClient consulClient;
    private final LeaderElectionConfig config;
    private final HealthCheckProvider healthCheckProvider;

    private final AtomicBoolean started = new AtomicBoolean(false);
    private final AtomicBoolean isLeader = new AtomicBoolean(false);
    private final AtomicLong lockVersion = new AtomicLong(-1);

    public boolean isLeader() {
        return isLeader.get();
    }

    @EventListener
    public void startWhenReady(ApplicationReadyEvent event) {
        log.info("Starting leader election..");
        started.compareAndSet(false, true);
    }

    @Scheduled(fixedDelayString = "${leader.election.polling-delay}")
    private void competeForLeader() {
        if (started.get()) {
            QueryParams queryParams = new QueryParams(config.getPollingDuration(), lockVersion.get());
            Response<GetValue> lock = consulClient.getKVValue(config.getLockKey(), config.getToken(), queryParams);

            if (hasLeader(lock.getValue())) {
                String leaderId = lock.getValue().getDecodedValue();
                log.debug("Current leader: {} ({})", leaderId, lock.getConsulIndex());

                lockVersion.set(lock.getConsulIndex());
                isLeader.set(config.getInstanceId().equals(leaderId));
            } else {
                isLeader.set(acquireLeadership(config.getInstanceId()));
            }
        }
    }

    private boolean hasLeader(GetValue lockValue) {
        return lockValue != null && lockValue.getSession() != null;
    }

    private boolean acquireLeadership(String leaderId) {
        Response<Boolean> response = consulClient.setKVValue(config.getLockKey(), leaderId, config.getToken(), new PutParams() {{
            setAcquireSession(createSession());
        }});

        if (response.getValue()) {
            log.info("Acquired leader lock with ID: {}", leaderId);
            return true;

        } else {
            log.info("Failed to acquire the lock as it is taken by another instance..");
            return false;
        }
    }

    private String createSession() {
        NewSession newSession = new NewSession();
        newSession.setChecks(healthCheckProvider.getChecks());

        Response<String> session =  consulClient.sessionCreate(newSession, QueryParams.DEFAULT);
        return session.getValue();
    }

}
