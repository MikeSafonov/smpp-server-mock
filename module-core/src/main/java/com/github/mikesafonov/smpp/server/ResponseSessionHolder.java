package com.github.mikesafonov.smpp.server;

import com.cloudhopper.smpp.SmppServerSession;
import lombok.EqualsAndHashCode;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

/**
 * Holder for sessions with RECEIVER and TRANSCEIVER types
 *
 * @author Mike Safonov
 */
@EqualsAndHashCode
public class ResponseSessionHolder {
    @Getter
    private List<SmppServerSession> sessions = new ArrayList<>();

    public void add(SmppServerSession session) {
        sessions.add(session);
    }

    public void remove(SmppServerSession session) {
        sessions.remove(session);
    }
}
