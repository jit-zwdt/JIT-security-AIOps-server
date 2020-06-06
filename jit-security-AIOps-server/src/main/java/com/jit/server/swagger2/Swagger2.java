package com.jit.server.swagger2;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * @author zengxin_miao
 * @version 1.0
 * @date 2020.06.06
 */

@Configuration
@EnableSwagger2
public class Swagger2 {

    /**
     * Create API application apiInfo() add API related information.
     * Use select() function get a ApiSelectorBuilder instance,To control which interfaces are exposed to the Swagger，
     * This example uses the specified scan package path to define the directory where the API is to be created.
     */
    @Bean
    public Docket createRestApi() {
        return new Docket(DocumentationType.SWAGGER_2).apiInfo(apiInfo()).select()
                .apis(RequestHandlerSelectors.basePackage("com.jit.server.controller")).paths(PathSelectors.any()).build();
    }

    /**
     * Create API base info (this information will show in word page) access address : http://ip:port:contenct/swagger-ui.html
     */
    private ApiInfo apiInfo() {
        Contact contact = new Contact("author", "url", "email address");
        return new ApiInfoBuilder().title("AIOps server : APIs")
                .description("service : publish service APIs").termsOfServiceUrl("http://127.0.0.1:8080/swagger-ui.html")
                .contact(contact).version("1.0").build();
    }

}
