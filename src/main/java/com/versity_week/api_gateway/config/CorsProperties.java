package com.versity_week.api_gateway.config;

import java.util.List;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "gateway.cors")
public record CorsProperties(List<String> allowedOrigins) {

	public CorsProperties {
		allowedOrigins = allowedOrigins == null || allowedOrigins.isEmpty() ? List.of("*") : allowedOrigins;
	}
}
