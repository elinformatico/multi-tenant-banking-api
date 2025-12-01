package com.banking.config;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * TenantFilter - Intercepts all HTTP requests to extract and set tenant context
 *
 * This filter:
 * 1. Extracts the X-Tenant-Id header from each incoming request
 * 2. Sets it in TenantContext for use throughout the request
 * 3. Validates that a tenant ID is provided
 * 4. Cleans up the context after request completion
 *
 * The @Component annotation makes this a Spring-managed bean
 * Spring Boot automatically registers it as a filter
 */
@Component
public class TenantFilter implements Filter {

    private static final String TENANT_HEADER = "X-Tenant-Id";

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;

        // Extract tenant ID from request header
        String tenantId = httpRequest.getHeader(TENANT_HEADER);

        // Validate tenant ID is present
        if (tenantId == null || tenantId.trim().isEmpty()) {
            httpResponse.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            httpResponse.setContentType("application/json");
            httpResponse.getWriter().write("{\"error\": \"Missing or invalid X-Tenant-Id header\"}");
            return;
        }

        try {
            // Set tenant context for this request thread
            TenantContext.setTenantId(tenantId);

            // Continue with the request chain
            chain.doFilter(request, response);
        } finally {
            // Always clear the context to prevent memory leaks
            // This is crucial in servlet containers that use thread pools
            TenantContext.clear();
        }
    }
}
