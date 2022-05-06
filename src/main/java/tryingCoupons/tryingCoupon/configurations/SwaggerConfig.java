package tryingCoupons.tryingCoupon.configurations;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.*;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spi.service.contexts.SecurityContext;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.Collections;
import java.util.List;


@Configuration
@EnableSwagger2
public class SwaggerConfig {
    private static final String BEARER_AUTH = "Bearer";

    /**
     * Swagger configuration bean
     * @return - A Docket of swagger
     */
    @Bean
    public Docket api() {
        return new Docket(DocumentationType.SWAGGER_2).select().apis(RequestHandlerSelectors.basePackage("tryingCoupons.tryingCoupon")).paths(PathSelectors.any()).build().apiInfo(apiInfo())
                .securitySchemes(securitySchemes()).securityContexts(List.of(securityContext()))
                .securityContexts(List.of(securityContext2()))
                .securityContexts(List.of(securityContext3()));
    }

    /**
     * the info of an api
     * @return - returns ApiInfo object that contains info.
     */
    private ApiInfo apiInfo() {
        return new ApiInfo("Coupons API", "HETI COUPON", null, "WELCOME",
                new Contact("Login", "http://localhost:8080",""), "Logout", "http://localhost:8080/logout", Collections.emptyList());
    }


    /**
     * List of security scheme
     * @return List of the security scheme
     */
    private List<SecurityScheme> securitySchemes() {
        return List.of( new ApiKey(BEARER_AUTH, "Authorization","header"));
    }

    /**
     * which path to secure with swagger
     * @return SecurityContext object that contains an admin path.
     */
    private SecurityContext securityContext() {
        return SecurityContext.builder().securityReferences(List.of( bearerAuthReference()))
                .forPaths(PathSelectors.ant("/admin/**"))
                .build();
    }

    /**
     * which path to secure with swagger
     * @return SecurityContext object that contains a company path.
     */
    private SecurityContext securityContext2() {
        return SecurityContext.builder().securityReferences(List.of( bearerAuthReference()))
                .forPaths(PathSelectors.ant("/company/**"))
                .build();
    }

    /**
     * which path to secure with swagger
     * @return SecurityContext object that contains a customer path.
     */
    private SecurityContext securityContext3() {
        return SecurityContext.builder().securityReferences(List.of( bearerAuthReference()))
                .forPaths(PathSelectors.ant("/customer/**"))
                .build();
    }


    /**
     * Configure the security reference
     * @return SecurityReference.
     */
    private SecurityReference bearerAuthReference() {
        return new SecurityReference(BEARER_AUTH, new AuthorizationScope[0]);
    }


}
