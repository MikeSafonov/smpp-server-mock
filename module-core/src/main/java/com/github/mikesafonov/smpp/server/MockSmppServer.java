package com.github.mikesafonov.smpp.server;

import com.cloudhopper.smpp.impl.DefaultSmppServer;
import com.cloudhopper.smpp.pdu.CancelSm;
import com.cloudhopper.smpp.pdu.PduRequest;
import com.cloudhopper.smpp.pdu.SubmitSm;
import com.cloudhopper.smpp.type.SmppChannelException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import lombok.Getter;

import static com.github.mikesafonov.smpp.server.Utils.checkPortOrGetFree;
import static com.github.mikesafonov.smpp.server.Utils.randomUuidName;

/**
 * SMPP server
 *
 * @author Mike Safonov
 */
public class MockSmppServer {
    private static final String HOST = "localhost";
    public static final int RANDOM_PORT = -1;

    public static final String DEFAULT_SYSTEM_ID = "mockSmppServer";
    public static final String DEFAULT_PASSWORD = "password";

    @Getter
    private final String name;
    @Getter
    private final int port;
    @Getter
    private final String systemId;
    @Getter
    private final String password;
    @Getter
    private final MockSmppServerHandler handler;
    private final DefaultSmppServer smppServer;
    @Getter
    private boolean started = false;

    public MockSmppServer() {
        this(randomUuidName());
    }

    public MockSmppServer(String name) {
        this(name, RANDOM_PORT);
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
        this(name, RANDOM_PORT, systemId, password);
    }

    public MockSmppServer(int port, String systemId, String password) {
        this(randomUuidName(), port, systemId, password);
    }

    public MockSmppServer(String name, int port, String systemId, String password) {
        this(name, port, systemId, password, new MockSmppServerHandler(systemId, password));
    }

    public MockSmppServer(String name, int port, String systemId, String password, MockSmppServerHandler handler) {
        this.name = name;
        this.port = checkPortOrGetFree(port);
        this.systemId = systemId;
        this.password = password;
        this.handler = handler;
        this.smppServer = new DefaultSmppServer(new MockSmppServerConfiguration(this.port, systemId),
                this.handler, Executors.newCachedThreadPool());
    }

    //test only
    MockSmppServer(int port, String systemId, String password,
                   MockSmppServerHandler handler, DefaultSmppServer smppServer) {
        this.name = randomUuidName();
        this.port = port;
        this.systemId = systemId;
        this.password = password;
        this.handler = handler;
        this.smppServer = smppServer;
    }

    /**
     * Start mock server
     */
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

    /**
     * Stop mock server
     */
    public void stop() {
        smppServer.stop();
        started = false;
    }

    public int getCountRequests() {
        return handler.getSessionHandler().countTotalMessages();
    }

    /**
     * Delete all incoming requests
     */
    public void clearRequests() {
        handler.getSessionHandler().clear();
    }

    /**
     * @return list of all received {@link PduRequest}
     */
    public List<PduRequest> getRequests() {
        return new ArrayList<>(handler.getSessionHandler().getReceivedPduRequests());
    }

    /**
     * @return list of received {@link SubmitSm}
     */
    public List<SubmitSm> getSubmitSmMessages() {
        return new ArrayList<>(handler.getSessionHandler().getSubmitSms());
    }

    /**
     * @return list of received {@link CancelSm}
     */
    public List<CancelSm> getCancelSmMessages() {
        return new ArrayList<>(handler.getSessionHandler().getCancelSms());
    }

    /**
     * @return server description (name, port, systemId)
     */
    public String getDescription() {
        return "Smpp server[name: " + name + ", port: " + port + ", systemId: " + systemId + "]";
    }

    /**
     * @return always localhost
     */
    public String getHost(){
        return HOST;
    }
}
