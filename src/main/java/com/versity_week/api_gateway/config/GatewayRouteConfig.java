package com.versity_week.api_gateway.config;

import static org.springframework.cloud.gateway.server.mvc.filter.BeforeFilterFunctions.uri;
import static org.springframework.cloud.gateway.server.mvc.handler.GatewayRouterFunctions.route;
import static org.springframework.cloud.gateway.server.mvc.handler.HandlerFunctions.http;
import static org.springframework.web.servlet.function.RequestPredicates.path;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.function.RouterFunction;
import org.springframework.web.servlet.function.ServerResponse;

@Configuration
public class GatewayRouteConfig {

	private final GatewayServiceProperties properties;

	public GatewayRouteConfig(GatewayServiceProperties properties) {
		this.properties = properties;
	}

	@Bean
	public RouterFunction<ServerResponse> authServiceRoutes() {
		return route("auth-service")
				.route(path("/api/v1/auth/**"), http())
				.route(path("/api/v1/colleges/**"), http())
				.route(path("/api/v1/users/**"), http())
				.route(path("/api/v1/participants/**"), http())
				.route(path("/api/v1/organizers/**"), http())
				.before(uri(properties.authServiceUrl()))
				.build();
	}

	@Bean
	public RouterFunction<ServerResponse> eventServiceRoutes() {
		return route("event-service")
				.route(path("/api/v1/events/**"), http())
				.before(uri(properties.eventServiceUrl()))
				.build();
	}

	@Bean
	public RouterFunction<ServerResponse> registrationServiceRoutes() {
		return route("registration-service")
				.route(path("/api/v1/registrations/**"), http())
				.before(uri(properties.registrationServiceUrl()))
				.build();
	}
}
