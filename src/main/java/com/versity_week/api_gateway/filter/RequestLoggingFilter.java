package com.versity_week.api_gateway.filter;

import java.io.IOException;
import java.util.Collections;
import java.util.Enumeration;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletRequestWrapper;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class RequestLoggingFilter extends OncePerRequestFilter {

	public static final String CORRELATION_ID_HEADER = "X-Request-Id";

	private static final Logger log = LoggerFactory.getLogger(RequestLoggingFilter.class);

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		long startTime = System.currentTimeMillis();
		String correlationId = resolveCorrelationId(request);
		response.setHeader(CORRELATION_ID_HEADER, correlationId);

		HttpServletRequest requestToUse = new CorrelationIdRequestWrapper(request, correlationId);

		try {
			filterChain.doFilter(requestToUse, response);
		} finally {
			long durationMs = System.currentTimeMillis() - startTime;
			log.info("method={} path={} status={} durationMs={} correlationId={}",
					request.getMethod(), request.getRequestURI(), response.getStatus(), durationMs, correlationId);
		}
	}

	private String resolveCorrelationId(HttpServletRequest request) {
		String provided = request.getHeader(CORRELATION_ID_HEADER);
		return provided != null && !provided.isBlank() ? provided : UUID.randomUUID().toString();
	}

	private static final class CorrelationIdRequestWrapper extends HttpServletRequestWrapper {

		private final String correlationId;

		private CorrelationIdRequestWrapper(HttpServletRequest request, String correlationId) {
			super(request);
			this.correlationId = correlationId;
		}

		@Override
		public String getHeader(String name) {
			return CORRELATION_ID_HEADER.equalsIgnoreCase(name) ? correlationId : super.getHeader(name);
		}

		@Override
		public Enumeration<String> getHeaders(String name) {
			if (CORRELATION_ID_HEADER.equalsIgnoreCase(name)) {
				return Collections.enumeration(Collections.singletonList(correlationId));
			}
			return super.getHeaders(name);
		}

		@Override
		public Enumeration<String> getHeaderNames() {
			Set<String> names = new LinkedHashSet<>();
			Collections.list(super.getHeaderNames()).forEach(name -> {
				if (!CORRELATION_ID_HEADER.equalsIgnoreCase(name)) {
					names.add(name);
				}
			});
			names.add(CORRELATION_ID_HEADER);
			return Collections.enumeration(names);
		}
	}
}
