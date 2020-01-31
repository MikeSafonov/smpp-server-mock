package com.github.mikesafonov.smpp.assertj;

import com.github.mikesafonov.smpp.server.MockSmppServer;
import org.assertj.core.api.ListAssert;

import java.util.List;

/**
 * @author Mike Safonov
 */
public class MockSmppServerListAssert extends ListAssert<MockSmppServer> {
    public MockSmppServerListAssert(List<? extends MockSmppServer> actual) {
        super(actual);
    }
}
