package com.whizspider.gateway;

import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.netflix.zuul.EnableZuulProxy;
import org.springframework.stereotype.Component;

@Component
class DataSetup implements ApplicationRunner {
  @Override
  public void run(ApplicationArguments args) {
  }
}

@SpringBootApplication(scanBasePackages = {"com.whizspider.gateway"})
@EnableZuulProxy
@EnableEurekaClient
public class GatewayApplication {

  public static void main(String[] args) {
    SpringApplication.run(GatewayApplication.class, args);
  }
}
