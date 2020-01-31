package com.github.mikesafonov.smpp.assertj;

import com.github.mikesafonov.smpp.server.MockSmppServerHolder;
import org.assertj.core.api.AbstractAssert;


/**
 * @author Mike Safonov
 */
public class MockSmppServerHolderAssert extends AbstractAssert<MockSmppServerHolderAssert, MockSmppServerHolder> {
    public MockSmppServerHolderAssert(MockSmppServerHolder mockSmppServerHolder) {
        super(mockSmppServerHolder, MockSmppServerHolderAssert.class);
    }

    public MockSmppServerListAssert servers() {
        return new MockSmppServerListAssert(actual.getServers());
    }

    public MockSmppServerHolderAssert allStarted() {
        isNotNull();
        if (!actual.isAllStarted()) {
            failWithMessage("Expected all servers started");
        }
        return this;
    }

    public MockSmppServerAssert serverByName(String name) {
        isNotNull();
        return actual.getByName(name).map(MockSmppServerAssert::new)
                .orElseGet(() -> {
                            failWithMessage("Expected server with name " + name + " but not found");
                            return null;
                        }
                );
    }
}
