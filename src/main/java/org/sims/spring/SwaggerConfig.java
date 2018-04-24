package org.sims.spring;

import io.swagger.annotations.Api;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.Collections;
import com.google.common.base.Predicate;
import com.google.common.base.Predicates;

import static springfox.documentation.builders.PathSelectors.regex;

@Configuration
@EnableSwagger2
public class SwaggerConfig {
  @Bean
  public Docket api() {
    return new Docket(DocumentationType.SWAGGER_2)
            .select()
            .apis(RequestHandlerSelectors.basePackage("org.sims"))
            .paths(regex("/api.*"))
            .build()
            .apiInfo(apiInfo());
  }

  private ApiInfo apiInfo() {

    return new ApiInfo(
            "Service Inventory Mangement System (SIMS)",
            "Implementation of tmforum Product Inventory Management API REST Specificaiton, made for FFI (The Norwegian Defence Research Establishmen)",
            "1.0",
            "Terms of Service",
            new Contact("FFI", "www.ffi.no", "Frank-Trethan.Johnsen@ffi.no"),
            "Apache Licence Version 2.0",
            "https://www.apache.org/license.html", Collections.emptyList());
  }
}
