package com.versity_week.api_gateway.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "gateway.services")
public record GatewayServiceProperties(
		String authServiceUrl,
		String eventServiceUrl,
		String registrationServiceUrl
) {

	public GatewayServiceProperties {
		authServiceUrl = authServiceUrl != null ? authServiceUrl : "http://localhost:8081";
		eventServiceUrl = eventServiceUrl != null ? eventServiceUrl : "http://localhost:8080";
		registrationServiceUrl = registrationServiceUrl != null ? registrationServiceUrl : "http://localhost:8082";
	}
}
