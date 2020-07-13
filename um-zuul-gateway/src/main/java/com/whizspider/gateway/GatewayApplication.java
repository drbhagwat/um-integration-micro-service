package com.whizspider.gateway;

import com.whizspider.gateway.api.repo.UserRepository;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.netflix.zuul.EnableZuulProxy;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.stereotype.Component;

@Component
class DataSetup implements ApplicationRunner {
  @Override
  public void run(ApplicationArguments args) {
  }
}

@SpringBootApplication
@EnableZuulProxy
@EnableEurekaClient
@EnableJpaRepositories(basePackageClasses = UserRepository.class)
public class GatewayApplication {
  public static void main(String[] args) {
    SpringApplication.run(GatewayApplication.class, args);
  }
}
