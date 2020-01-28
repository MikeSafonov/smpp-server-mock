package com.github.mikesafonov.junit.smpp;

import org.junit.jupiter.api.extension.AfterAllCallback;
import org.junit.jupiter.api.extension.BeforeEachCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.TestInstancePostProcessor;
import org.junit.platform.commons.util.AnnotationUtils;

import java.lang.reflect.Field;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.logging.Logger;

import static com.github.mikesafonov.junit.smpp.TestUtils.findRandomOpenPortOnAllLocalInterfaces;

public class SmppExtension implements TestInstancePostProcessor, AfterAllCallback, BeforeEachCallback {
    private static final Logger logger = Logger.getLogger(SmppExtension.class.getName());

    private final Set<MockSmppServer> smppServers = new HashSet<>();

    @Override
    public void postProcessTestInstance(Object testInstance, ExtensionContext context) throws IllegalAccessException {
        List<Field> annotatedFields = AnnotationUtils.findAnnotatedFields(testInstance.getClass(),
                SmppServer.class, field -> field.getType() == MockSmppServer.class);
        for (Field annotatedField : annotatedFields) {
            createServer(testInstance, annotatedField);
        }
    }

    @Override
    public void afterAll(ExtensionContext context) {
        for (MockSmppServer smppServer : smppServers) {
            logger.info("Stopping server on port: " + smppServer.getPort() + " with systemId: " + smppServer.getSystemId());
            smppServer.stop();
            logger.info("Server on port: " + smppServer.getPort() + " stopped");
        }
    }

    private void createServer(Object testInstance, Field field) throws IllegalAccessException {
        SmppServer annotation = field.getAnnotation(SmppServer.class);
        int port = annotation.port();
        if (port == SmppServer.RANDOM_PORT) {
            port = findRandomOpenPortOnAllLocalInterfaces();
        }
        logger.info("Creating server on port: " + port + " with systemId: " + annotation.systemId());
        MockSmppServer server = new MockSmppServer(port, annotation.systemId(), annotation.password());
        smppServers.add(server);
        field.setAccessible(true);
        field.set(testInstance, server);
    }

    @Override
    public void beforeEach(ExtensionContext context) {
        for (MockSmppServer smppServer : smppServers) {
            logger.info("Starting server on port: " + smppServer.getPort() + " with systemId: " + smppServer.getSystemId());
            smppServer.start();
            logger.info("Server on port: " + smppServer.getPort() + " started");
        }
    }
}
