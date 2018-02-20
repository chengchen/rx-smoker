package com.edgelab.marketdata.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotNull;

@ConfigurationProperties(prefix = "leader.election")
@Data
@Validated
class LeaderElectionConfig {

    @NotNull
    private String lockKey;

    @NotNull
    private String instanceId;

    @NotNull
    private String token;

    @NotNull
    private Long pollingDuration;

    @NotNull
    private Long pollingDelay;

}
