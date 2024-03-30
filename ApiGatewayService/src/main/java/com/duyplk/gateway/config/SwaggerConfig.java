package com.duyplk.gateway.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.cloud.gateway.route.Route;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.netflix.eureka.EurekaServiceInstance;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import reactor.core.publisher.Flux;
import springfox.documentation.oas.annotations.EnableOpenApi;
import springfox.documentation.swagger.web.SwaggerResource;
import springfox.documentation.swagger.web.SwaggerResourcesProvider;

import java.util.ArrayList;
import java.util.List;


@Configuration
@EnableOpenApi
public class SwaggerConfig {

    @Value("${spring.application.name}")
    private String appName;
    public static final String API_URI = "/v3/api-docs";
    private final RouteLocator routeLocator;
    private final DiscoveryClient discoveryClient;

    public SwaggerConfig(RouteLocator routeLocator, DiscoveryClient discoveryClient) {
        this.routeLocator = routeLocator;
        this.discoveryClient = discoveryClient;
    }

    @Bean
    @Primary
    public SwaggerResourcesProvider swaggerResourcesProvider() {
        return () -> {
            List<SwaggerResource> resources = new ArrayList<>();
            Flux<Route> routes = routeLocator.getRoutes();
            routes.subscribe(route -> {
                String serviceId = route.getId().substring(route.getId().indexOf("_") + 1).toLowerCase();
                if (!serviceId.equalsIgnoreCase(appName)) {
                    EurekaServiceInstance serviceInstance = (EurekaServiceInstance) discoveryClient.getInstances(serviceId).get(0);
                    resources.add(swaggerResource(serviceInstance.getInstanceInfo().getAppName(), serviceId));
                }
            });
            return resources;
        };
    }

    private SwaggerResource swaggerResource(String name, String serviceId) {
        SwaggerResource swaggerResource = new SwaggerResource();
        swaggerResource.setName(name.replaceAll("\\-", " "));
        swaggerResource.setLocation("/" + serviceId + API_URI);
        swaggerResource.setSwaggerVersion("1.0");
        return swaggerResource;
    }

}
