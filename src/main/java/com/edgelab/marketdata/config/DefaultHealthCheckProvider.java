package com.edgelab.marketdata.config;

import com.google.common.collect.ImmutableList;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class DefaultHealthCheckProvider implements HealthCheckProvider {

    @Override
    public List<String> getChecks() {
        return ImmutableList.of("serfHealth", "service:rx-smoker");
    }

}
