package com.github.mikesafonov.junit.smpp;

import com.cloudhopper.smpp.SmppServerConfiguration;

/**
 * @author Mike Safonov
 */
public class MockSmppServerConfiguration extends SmppServerConfiguration {
    public MockSmppServerConfiguration(int port, String systemId) {
        super();
        this.setPort(port);
        this.setSystemId(systemId);
    }
}
