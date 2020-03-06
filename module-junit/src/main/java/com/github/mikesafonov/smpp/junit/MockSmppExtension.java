package com.github.mikesafonov.smpp.junit;

import com.github.mikesafonov.smpp.server.MockSmppServer;
import org.junit.jupiter.api.extension.*;
import org.junit.platform.commons.util.AnnotationUtils;

import java.lang.reflect.Field;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

public class MockSmppExtension implements
        TestInstancePostProcessor, AfterAllCallback,
        BeforeEachCallback, AfterEachCallback {
    private static final Logger logger = Logger.getLogger(MockSmppExtension.class.getName());

    private final Set<Server> smppServers = new HashSet<>();

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
        for (Server smppServer : smppServers) {
            logger.info("Stopping " + smppServer.value.getDescription());
            smppServer.value.stop();
            logger.info(smppServer.value.getDescription() + " stopped");
        }
    }

    @Override
    public void beforeEach(ExtensionContext context) {
        for (Server smppServer : smppServers) {
            logger.info("Starting " + smppServer.value.getDescription());
            smppServer.value.start();
            logger.info(smppServer.value.getDescription() + " started");
        }
    }

    @Override
    public void afterEach(ExtensionContext context) {
        smppServers.forEach(server -> {
            if (server.config.clearAfterTest()) {
                server.value.clearRequests();
            }
        });
    }

    private void createServer(Object testInstance, Field field) throws IllegalAccessException {
        SmppServer annotation = field.getAnnotation(SmppServer.class);
        int port = annotation.port();
        MockSmppServer server;
        String name = annotation.name();
        if (name.isEmpty()) {
            logger.log(Level.INFO, "Creating server on port: {0} with systemId: {1}", new Object[]{port, annotation.systemId()});
            server = new MockSmppServer(port, annotation.systemId(), annotation.password());
        } else {
            logger.log(Level.INFO, "Creating server {0} on port: {1} with systemId: {2}", new Object[]{name, port, annotation.systemId()});
            server = new MockSmppServer(name, port, annotation.systemId(), annotation.password());
        }

        smppServers.add(new Server(annotation, server));
        field.setAccessible(true);
        field.set(testInstance, server);
    }

    private static class Server {
        private SmppServer config;
        private MockSmppServer value;

        public Server(SmppServer config, MockSmppServer server) {
            this.config = config;
            this.value = server;
        }
    }
}
