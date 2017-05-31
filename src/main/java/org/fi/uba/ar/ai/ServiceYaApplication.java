package org.fi.uba.ar.ai;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.vaadin.spring.security.annotation.EnableVaadinManagedSecurity;

@SpringBootApplication(exclude = org.springframework.boot.autoconfigure.security.SecurityAutoConfiguration.class)
@EnableVaadinManagedSecurity
public class ServiceYaApplication {

  public static void main(String[] args) {
    SpringApplication.run(ServiceYaApplication.class, args);
  }
}
