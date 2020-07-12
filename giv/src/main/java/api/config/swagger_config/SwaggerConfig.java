package api.config.swagger_config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import static springfox.documentation.builders.PathSelectors.regex;

/**
 * @author : Sachin Kulkarni
 * @date : 30-09-2019
 */
@EnableSwagger2
@Configuration
public class SwaggerConfig {
  @Bean
  public Docket coreApi() {

    return new Docket(DocumentationType.SWAGGER_2)
        .groupName("Core")
        .select()
        .apis(RequestHandlerSelectors.basePackage("api.core"))
        .paths(regex(".*"))
        .build();
  }

  @Bean
  public Docket ExternalApi() {

    return new Docket(DocumentationType.SWAGGER_2)
        .groupName("External")
        .select()
        .apis(RequestHandlerSelectors.basePackage("api.external"))
        .paths(regex(".*"))
        .build();
  }
}
