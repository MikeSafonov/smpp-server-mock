package com.github.mikesafonov.junit.smpp;

import com.cloudhopper.smpp.impl.DefaultSmppServer;
import com.cloudhopper.smpp.pdu.CancelSm;
import com.cloudhopper.smpp.pdu.PduRequest;
import com.cloudhopper.smpp.pdu.SubmitSm;
import com.cloudhopper.smpp.type.SmppChannelException;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.Executors;

public class MockSmppServer {
    static final int DEFAULT_PORT = 2077;
    static final String DEFAULT_SYSTEM_ID = "mockSmppServer";
    static final String DEFAULT_PASSWORD = "password";

    private final String name;
    private final int port;
    private final String systemId;
    private final MockSmppServerHandler handler;
    private final DefaultSmppServer smppServer;
    private boolean started = false;

    public MockSmppServer() {
        this(randomUuidName());
    }

    public MockSmppServer(String name) {
        this(name, DEFAULT_PORT);
    }

    public MockSmppServer(int port) {
        this(randomUuidName(), port);
    }

    public MockSmppServer(String name, int port) {
        this(name, port, DEFAULT_SYSTEM_ID, DEFAULT_PASSWORD);
    }

    public MockSmppServer(String systemId, String password) {
        this(randomUuidName(), systemId, password);
    }

    public MockSmppServer(String name, String systemId, String password) {
        this(name, DEFAULT_PORT, systemId, password);
    }

    public MockSmppServer(int port, String systemId, String password) {
        this(port, systemId, password, new MockSmppServerConfiguration(port, systemId));
    }

    public MockSmppServer(String name, int port, String systemId, String password) {
        this(name, port, systemId, password, new MockSmppServerConfiguration(port, systemId));
    }

    public MockSmppServer(int port, String systemId, String password, MockSmppServerConfiguration configuration) {
        this(randomUuidName(), port, systemId, password, configuration);
    }

    public MockSmppServer(String name, int port, String systemId, String password, MockSmppServerConfiguration configuration) {
        this.name = name;
        this.port = port;
        this.systemId = systemId;
        this.handler = new MockSmppServerHandler(systemId, password);
        this.smppServer = new DefaultSmppServer(configuration, handler, Executors.newCachedThreadPool());
    }

    public MockSmppServer(int port, String systemId, MockSmppServerHandler handler, MockSmppServerConfiguration configuration) {
        this(port, systemId, handler, new DefaultSmppServer(configuration, handler, Executors.newCachedThreadPool()));
    }

    public MockSmppServer(int port, String systemId, MockSmppServerHandler handler, DefaultSmppServer smppServer) {
        this(randomUuidName(), port, systemId, handler, smppServer);
    }

    public MockSmppServer(String name, int port, String systemId, MockSmppServerHandler handler, DefaultSmppServer smppServer) {
        this.name = name;
        this.port = port;
        this.systemId = systemId;
        this.handler = handler;
        this.smppServer = smppServer;
    }

    public void start() {
        if (started) {
            return;
        }
        try {
            smppServer.start();
            started = true;
        } catch (SmppChannelException e) {
            throw new RuntimeException(e);
        }
    }

    public void stop() {
        smppServer.stop();
    }

    public int getPort() {
        return port;
    }

    public String getSystemId() {
        return systemId;
    }

    public int countMessages() {
        return handler.countTotalMessages();
    }

    public List<PduRequest> getMessages() {
        return new ArrayList<>(handler.getSessionHandler().getReceivedPduRequests());
    }

    public List<SubmitSm> getSubmitSmMessages() {
        return new ArrayList<>(handler.getSessionHandler().getSubmitSms());
    }

    public List<CancelSm> getCancelSmMessages() {
        return new ArrayList<>(handler.getSessionHandler().getCancelSms());
    }

    public boolean isStarted() {
        return started;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return "Smpp server[name: " + name + ", port: " + port + ", systemId: " + systemId + "]";
    }

    private static String randomUuidName() {
        return UUID.randomUUID().toString();
    }
}
