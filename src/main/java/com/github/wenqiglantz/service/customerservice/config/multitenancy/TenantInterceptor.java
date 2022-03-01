package com.github.wenqiglantz.service.customerservice.config.multitenancy;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import org.springframework.ui.ModelMap;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.context.request.WebRequestInterceptor;

import static com.github.wenqiglantz.service.customerservice.config.multitenancy.TenantConstants.X_TENANT_ID;
import static com.github.wenqiglantz.service.customerservice.config.multitenancy.TenantConstants.ZERO;

@Component
public class TenantInterceptor implements WebRequestInterceptor {

    private final String defaultTenant;

    @Autowired
    public TenantInterceptor(
            @Value("${multitenancy.tenant.default-tenant:#{null}}") String defaultTenant) {
        this.defaultTenant = defaultTenant;
    }

    @Override
    public void preHandle(WebRequest request) {
        String tenantId = ZERO;
        if (request.getHeader(X_TENANT_ID) != null) {
            tenantId = request.getHeader(X_TENANT_ID);
        }
        TenantContext.setTenantId(tenantId);
    }

    @Override
    public void postHandle(@NonNull WebRequest request, ModelMap model) {
        TenantContext.clear();
    }

    @Override
    public void afterCompletion(@NonNull WebRequest request, Exception ex) {
        // NOOP
    }
}
