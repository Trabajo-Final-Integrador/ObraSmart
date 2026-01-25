package com.obrasmart.mediaservice.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory;
import org.springframework.boot.web.server.WebServerFactoryCustomizer;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.List;
import java.util.Optional;

@Component
public class PortFallbackConfig implements WebServerFactoryCustomizer<TomcatServletWebServerFactory> {

  private final Environment environment;
  private static final List<Integer> CANDIDATE_PORTS = List.of(8090, 8091, 8092, 8093, 8094, 8095, 8096, 8097, 8098, 8099);

  @Autowired
  public PortFallbackConfig(Environment environment) {
    this.environment = environment;
  }

  @Override
  public void customize(TomcatServletWebServerFactory factory) {
    // If server.port is explicitly set (env, system prop, etc.), respect it and do not override
    String configuredPort = environment.getProperty("server.port");
    if (configuredPort != null && !configuredPort.isBlank()) {
      return;
    }

    Optional<Integer> freePort = CANDIDATE_PORTS.stream().filter(this::isPortAvailable).findFirst();
    freePort.ifPresent(factory::setPort);
  }

  private boolean isPortAvailable(int port) {
    try (ServerSocket socket = new ServerSocket(port)) {
      socket.setReuseAddress(true);
      return true;
    } catch (IOException e) {
      return false;
    }
  }
}
