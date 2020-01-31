package com.github.mikesafonov.smpp.boot;

import com.github.mikesafonov.smpp.server.MockSmppServer;
import com.github.mikesafonov.smpp.server.MockSmppServerHolder;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.MapPropertySource;

import java.util.LinkedHashMap;
import java.util.List;

import static java.util.stream.Collectors.toList;
import static org.springframework.core.Ordered.HIGHEST_PRECEDENCE;

/**
 * @author Mike Safonov
 */
@Configuration
@Order(HIGHEST_PRECEDENCE)
@EnableConfigurationProperties(MockSmppProperties.class)
public class MockSmppBootstrapConfiguration {

    @Bean(destroyMethod = "stopAll")
    public MockSmppServerHolder mockSmppServerHolder(MockSmppProperties smppProperties, ConfigurableEnvironment environment) {
        List<MockSmppServer> servers = smppProperties.getMocks().entrySet().stream()
                .map(entry -> createServer(entry.getKey(), entry.getValue()))
                .collect(toList());
        MockSmppServerHolder holder = new MockSmppServerHolder(servers);
        holder.startAll();
        registerEnvironment(servers, environment);
        return holder;
    }

    private MockSmppServer createServer(String name, MockSmppProperties.SMPP smpp) {
        return new MockSmppServer(name, smpp.getPort(), smpp.getSystemId(), smpp.getPassword());
    }

    private void registerEnvironment(List<MockSmppServer> servers, ConfigurableEnvironment environment) {
        LinkedHashMap<String, Object> map = new LinkedHashMap<>();
        servers.forEach(server -> {
            map.put("smpp.mocks." + server.getName() + ".port", server.getPort());
            map.put("smpp.mocks." + server.getName() + ".password", server.getPassword());
            map.put("smpp.mocks." + server.getName() + ".systemId", server.getSystemId());
        });


        MapPropertySource propertySource = new MapPropertySource("mockSmppEnvironment", map);
        environment.getPropertySources().addFirst(propertySource);
    }
}
