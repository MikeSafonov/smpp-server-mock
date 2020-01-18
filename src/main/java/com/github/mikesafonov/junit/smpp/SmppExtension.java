package com.github.mikesafonov.junit.smpp;

import org.junit.jupiter.api.extension.*;
import org.junit.platform.commons.util.AnnotationUtils;

import java.lang.reflect.Field;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class SmppExtension implements TestInstancePostProcessor, AfterAllCallback, BeforeEachCallback {
    private final Set<MockSmppServer> smppServers = new HashSet<>();

    @Override
    public void postProcessTestInstance(Object testInstance, ExtensionContext context) {
        List<Field> annotatedFields = AnnotationUtils.findAnnotatedFields(testInstance.getClass(),
                SmppServer.class, field -> field.getType() == MockSmppServer.class);
        annotatedFields.forEach(this::createServer);
    }

    @Override
    public void afterAll(ExtensionContext context) {
        smppServers.forEach(MockSmppServer::stop);
    }

    private void createServer(Field field) {
        SmppServer smppServer = field.getAnnotation(SmppServer.class);
        smppServers.add(new MockSmppServer(smppServer.port(), smppServer.systemId(), smppServer.password()));
    }

    @Override
    public void beforeEach(ExtensionContext context) {
        smppServers.forEach(MockSmppServer::start);
    }
}
