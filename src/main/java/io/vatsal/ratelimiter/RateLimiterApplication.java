package io.vatsal.ratelimiter;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@SpringBootApplication
@EnableSwagger2
@EnableAspectJAutoProxy(proxyTargetClass = true)
public class RateLimiterApplication {

  public static void main(String[] args) {
    SpringApplication.run(RateLimiterApplication.class, args);
  }

  @Bean
  public Docket swaggerConfig() {
    return new Docket(DocumentationType.SWAGGER_2)
        .select()
        .paths(PathSelectors.ant("/random/*"))
        .apis(RequestHandlerSelectors.basePackage("io.vatsal"))
        .build();
  }
}
