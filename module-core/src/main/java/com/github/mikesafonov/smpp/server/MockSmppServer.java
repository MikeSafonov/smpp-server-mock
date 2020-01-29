package com.github.mikesafonov.smpp.server;

import com.cloudhopper.smpp.impl.DefaultSmppServer;
import com.cloudhopper.smpp.pdu.CancelSm;
import com.cloudhopper.smpp.pdu.PduRequest;
import com.cloudhopper.smpp.pdu.SubmitSm;
import com.cloudhopper.smpp.type.SmppChannelException;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;

import static com.github.mikesafonov.smpp.server.Utils.checkPortOrGetFree;
import static com.github.mikesafonov.smpp.server.Utils.randomUuidName;

/**
 * SMPP server
 *
 * @author Mike Safonov
 */
public class MockSmppServer {
    static final int DEFAULT_PORT = 2077;
    static final int RANDOM_PORT = -1;

    static final String DEFAULT_SYSTEM_ID = "mockSmppServer";
    static final String DEFAULT_PASSWORD = "password";

    private final String name;
    private final int port;
    private final String systemId;
    private final String password;
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
        this.name = randomUuidName();
        this.port = checkPortOrGetFree(port);
        this.systemId = systemId;
        this.password = password;
        this.handler = new MockSmppServerHandler(systemId, password);
        this.smppServer = new DefaultSmppServer(new MockSmppServerConfiguration(this.port, systemId),
                handler, Executors.newCachedThreadPool());
    }

    public MockSmppServer(String name, int port, String systemId, String password) {
        this.name = name;
        this.port = checkPortOrGetFree(port);
        this.systemId = systemId;
        this.password = password;
        this.handler = new MockSmppServerHandler(systemId, password);
        this.smppServer = new DefaultSmppServer(new MockSmppServerConfiguration(this.port, systemId),
                handler, Executors.newCachedThreadPool());
    }

    public MockSmppServer(int port, String systemId, String password, MockSmppServerConfiguration configuration) {
        this(randomUuidName(), port, systemId, password, configuration);
    }

    public MockSmppServer(String name, int port, String systemId, String password,
                          MockSmppServerConfiguration configuration) {
        this.name = name;
        this.port = port;
        this.systemId = systemId;
        this.password = password;
        this.handler = new MockSmppServerHandler(systemId, password);
        this.smppServer = new DefaultSmppServer(configuration, handler, Executors.newCachedThreadPool());
    }

    public MockSmppServer(int port, String systemId, String password,
                          MockSmppServerHandler handler, MockSmppServerConfiguration configuration) {
        this(port, systemId, password, handler,
                new DefaultSmppServer(configuration, handler, Executors.newCachedThreadPool()));
    }

    public MockSmppServer(int port, String systemId, String password,
                          MockSmppServerHandler handler, DefaultSmppServer smppServer) {
        this(randomUuidName(), port, systemId, password, handler, smppServer);
    }

    public MockSmppServer(String name, int port, String systemId, String password,
                          MockSmppServerHandler handler, DefaultSmppServer smppServer) {
        this.name = name;
        this.port = port;
        this.systemId = systemId;
        this.password = password;
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

    public boolean isStarted() {
        return started;
    }

    public String getName() {
        return name;
    }

    public String getPassword() {
        return password;
    }

    public int countMessages() {
        return handler.getSessionHandler().countTotalMessages();
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

    public String getDescription() {
        return "Smpp server[name: " + name + ", port: " + port + ", systemId: " + systemId + "]";
    }
}
