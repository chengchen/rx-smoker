package com.edgelab.marketdata.config;

import java.util.List;

public interface HealthCheckProvider {

    List<String> getChecks();

}
