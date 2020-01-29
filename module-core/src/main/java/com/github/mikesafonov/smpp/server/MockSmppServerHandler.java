package com.github.mikesafonov.smpp.server;

import com.cloudhopper.smpp.*;
import com.cloudhopper.smpp.pdu.BaseBind;
import com.cloudhopper.smpp.pdu.BaseBindResp;
import com.cloudhopper.smpp.type.SmppProcessingException;

import java.util.HashSet;
import java.util.Set;

/**
 * {@link SmppServerHandler} for {@link MockSmppServer}.
 *
 * @author Mike Safonov
 */
public class MockSmppServerHandler implements SmppServerHandler {
    private final Set<SmppServerSession> sessions;
    private final QueueSmppSessionHandler sessionHandler;
    private final String systemId;
    private final String password;

    public MockSmppServerHandler(String systemId, String password) {
        this(systemId, password, new QueueSmppSessionHandler());
    }

    public MockSmppServerHandler(String systemId, String password, QueueSmppSessionHandler sessionHandler) {
        this.systemId = systemId;
        this.password = password;
        this.sessions = new HashSet<>();
        this.sessionHandler = sessionHandler;
    }

    /**
     * Verifies systemId and password from {@link BaseBind}
     *
     * @param sessionId            smpp session id
     * @param sessionConfiguration session configuration
     * @param bindRequest          bind request
     * @throws SmppProcessingException when systemId or password dont match
     */
    @Override
    public void sessionBindRequested(Long sessionId, SmppSessionConfiguration sessionConfiguration,
                                     BaseBind bindRequest) throws SmppProcessingException {
        verifySystemId(bindRequest.getSystemId());
        verifyPassword(bindRequest.getPassword());
    }

    /**
     * Adds session to {@code sessions} Set and call {@link SmppServerSession#serverReady(SmppSessionHandler)}
     *
     * @param sessionId            smpp session id
     * @param session              smpp session
     * @param preparedBindResponse bind request response
     */
    @Override
    public void sessionCreated(Long sessionId, SmppServerSession session, BaseBindResp preparedBindResponse) {
        sessions.add(session);
        session.serverReady(sessionHandler);
    }

    /**
     * Removes session from {@code sessions} Set
     *
     * @param sessionId smpp session id
     * @param session   smpp session
     */
    @Override
    public void sessionDestroyed(Long sessionId, SmppServerSession session) {
        sessions.remove(session);
    }

    public Set<SmppServerSession> getSessions() {
        return sessions;
    }

    public QueueSmppSessionHandler getSessionHandler() {
        return sessionHandler;
    }

    private void verifySystemId(String incomingSystemId) throws SmppProcessingException {
        if (!systemId.equals(incomingSystemId)) {
            throw new SmppProcessingException(SmppConstants.STATUS_INVSYSID);
        }
    }

    private void verifyPassword(String incomingPassword) throws SmppProcessingException {
        if (!password.equals(incomingPassword)) {
            throw new SmppProcessingException(SmppConstants.STATUS_INVPASWD);
        }
    }
}
