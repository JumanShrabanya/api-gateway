package com.versity_week.api_gateway.config;

import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.filter.CorsFilter;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

@Configuration
public class CorsConfig {

	private final CorsProperties properties;

	public CorsConfig(CorsProperties properties) {
		this.properties = properties;
	}

	@Bean
	@Order(Ordered.HIGHEST_PRECEDENCE)
	public CorsFilter corsFilter() {
		CorsConfiguration configuration = new CorsConfiguration();
		if (properties.allowedOrigins().size() == 1 && "*".equals(properties.allowedOrigins().getFirst())) {
			configuration.addAllowedOriginPattern("*");
		} else {
			configuration.setAllowedOrigins(properties.allowedOrigins());
		}
		configuration.setAllowedMethods(List.of("GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS"));
		configuration.setAllowedHeaders(List.of("*"));
		configuration.setExposedHeaders(List.of("X-Request-Id"));
		configuration.setAllowCredentials(true);
		configuration.setMaxAge(3600L);

		UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
		source.registerCorsConfiguration("/**", configuration);
		return new CorsFilter(source);
	}
}
