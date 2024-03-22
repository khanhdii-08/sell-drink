package com.duyplk.core.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.servers.Server;
import lombok.Builder;

import java.util.List;

@Builder
public class SwaggerConfigBuilder {

    private final List<Server> servers;
    private final String infoTitle;
    private final String infoDescription;
    private final String infoVersion;
    private final String contactName;
    private final String contactEmail;
    private final String contactUrl;
    private final String licenseName;
    private final String licenseUrl;

    private SecurityScheme createAPIKeyScheme() {
        return new SecurityScheme()
                .type(SecurityScheme.Type.HTTP)
                .bearerFormat("JWT")
                .scheme("bearer");
    }

    public OpenAPI openAPI() {
        return new OpenAPI()
//                .addSecurityItem(new SecurityRequirement().addList("Bearer Authentication"))
                .components(new Components().addSecuritySchemes("Bearer Authentication", createAPIKeyScheme()))
                .servers(servers)
                .info(new Info()
                        .title(infoTitle)
                        .description(infoDescription)
                        .version(infoVersion)
                        .contact(new Contact().name(contactName).email(contactEmail).url(contactUrl))
                        .license(new License().name(licenseName).url(licenseUrl)));
    }
}
