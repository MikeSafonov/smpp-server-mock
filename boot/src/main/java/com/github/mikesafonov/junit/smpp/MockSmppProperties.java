package com.github.mikesafonov.junit.smpp;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Mike Safonov
 */
@Data
@ConfigurationProperties(prefix = "smpp")
public class MockSmppProperties {

    private Map<String, SMPP> mocks = new HashMap<>();

    @Data
    public static class SMPP {
        /**
         * Mock smpp server port
         */
        private int port = MockSmppServer.RANDOM_PORT;
        /**
         * Mock smpp server system id
         */
        private String systemId;
        /**
         * Mock smpp server password
         */
        private String password;
    }
}
