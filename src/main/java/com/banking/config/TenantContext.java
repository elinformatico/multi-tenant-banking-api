package com.banking.config;

/**
 * TenantContext - Thread-local storage for tenant identification
 *
 * This class stores the current tenant ID in a ThreadLocal variable,
 * ensuring that each request thread has its own isolated tenant context.
 * This is crucial for multi-tenancy as it prevents data leakage between
 * different tenant requests.
 *
 * ThreadLocal ensures that:
 * - Each HTTP request thread has its own tenant ID
 * - Concurrent requests don't interfere with each other
 * - The tenant ID is accessible throughout the request lifecycle
 */
public class TenantContext {

    private static final ThreadLocal<String> currentTenant = new ThreadLocal<>();

    /**
     * Set the tenant ID for the current thread/request
     */
    public static void setTenantId(String tenantId) {
        currentTenant.set(tenantId);
    }

    /**
     * Get the tenant ID for the current thread/request
     */
    public static String getTenantId() {
        return currentTenant.get();
    }

    /**
     * Clear the tenant ID after request processing
     * This prevents memory leaks in thread pools
     */
    public static void clear() {
        currentTenant.remove();
    }
}