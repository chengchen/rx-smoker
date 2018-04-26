package com.edgelab.marketdata.config;

import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@Component
public class DefaultHealthCheckProvider implements HealthCheckProvider {

    @Override
    public List<String> getChecks() {
        return Collections.unmodifiableList(Arrays.asList("serfHealth", "service:rx-smoker"));
    }

}
