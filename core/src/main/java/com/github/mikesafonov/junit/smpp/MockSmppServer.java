package com.github.mikesafonov.junit.smpp;

import com.cloudhopper.smpp.impl.DefaultSmppServer;
import com.cloudhopper.smpp.pdu.CancelSm;
import com.cloudhopper.smpp.pdu.PduRequest;
import com.cloudhopper.smpp.pdu.SubmitSm;
import com.cloudhopper.smpp.type.SmppChannelException;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;

public class MockSmppServer {
    static final int DEFAULT_PORT = 2077;
    static final String DEFAULT_SYSTEM_ID = "mockSmppServer";
    static final String DEFAULT_PASSWORD = "password";

    private final int port;
    private final String systemId;
    private final MockSmppServerHandler handler;
    private final DefaultSmppServer smppServer;
    private boolean started = false;

    public MockSmppServer() {
        this(DEFAULT_PORT);
    }

    public MockSmppServer(int port) {
        this(port, DEFAULT_SYSTEM_ID, DEFAULT_PASSWORD);
    }

    public MockSmppServer(String systemId, String password) {
        this(DEFAULT_PORT, systemId, password);
    }

    public MockSmppServer(int port, String systemId, String password) {
        this.port = port;
        this.systemId = systemId;
        this.handler = new MockSmppServerHandler(systemId, password);
        this.smppServer = new DefaultSmppServer(new MockSmppServerConfiguration(port, systemId), handler, Executors.newCachedThreadPool());
    }

    public MockSmppServer(int port, String systemId, String password, MockSmppServerConfiguration configuration) {
        this.port = port;
        this.systemId = systemId;
        this.handler = new MockSmppServerHandler(systemId, password);
        this.smppServer = new DefaultSmppServer(configuration, handler, Executors.newCachedThreadPool());
    }

    public MockSmppServer(int port, String systemId, MockSmppServerHandler handler, MockSmppServerConfiguration configuration) {
        this.port = port;
        this.systemId = systemId;
        this.handler = handler;
        this.smppServer = new DefaultSmppServer(configuration, handler, Executors.newCachedThreadPool());
    }

    public MockSmppServer(int port, String systemId, MockSmppServerHandler handler, DefaultSmppServer smppServer) {
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
            System.out.println("Starting smpp server on port " + port);
            smppServer.start();
            started = true;
            System.out.println("Server started on port " + port);
        } catch (SmppChannelException e) {
            throw new RuntimeException(e);
        }
    }

    public void stop() {
        smppServer.stop();
        System.out.println("Server stopped on port " + port);
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
}
