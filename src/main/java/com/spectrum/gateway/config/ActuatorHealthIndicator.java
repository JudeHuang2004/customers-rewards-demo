package com.spectrum.gateway.config;

import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.stereotype.Component;

@Component
public class ActuatorHealthIndicator implements HealthIndicator {

    public Health health() {
        int errorCode = check();
        if (errorCode != 0) {
            return Health.down().build();
        }
        return Health.up().build();
    }

    public int check() {
        // TODO out-of-scope, if needed, add customized health check code here.
        return 0;
    }

}
