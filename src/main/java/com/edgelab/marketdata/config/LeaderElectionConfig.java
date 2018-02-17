package com.edgelab.marketdata.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotNull;
import java.util.List;

@ConfigurationProperties(prefix = "leader.election")
@Data
@Validated
class LeaderElectionConfig {

    @NotNull
    private String selfId;

    @NotNull
    private String lockKey;

    @NotNull
    private List<String> checks;

    @NotNull
    private String pollingDelay;

}
