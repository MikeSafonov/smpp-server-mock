package com.github.mikesafonov.smpp.server;

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
