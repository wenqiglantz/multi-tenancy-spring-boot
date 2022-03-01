package com.github.wenqiglantz.service.customerservice.config.multitenancy;

public interface TenantAware {

    String getTenantId();

    void setTenantId(String tenantId);
}
