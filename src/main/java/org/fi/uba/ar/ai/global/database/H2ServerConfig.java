package org.fi.uba.ar.ai.global.database;

import java.sql.SQLException;
import org.h2.tools.Server;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class H2ServerConfig {

  @Value("${h2.tcp.port:9092}")
  private String port;

  @Bean(initMethod = "start", destroyMethod = "stop")
  public Server server() throws SQLException {
    return Server.createTcpServer("-tcpPort", port, "-tcpAllowOthers");
  }

}