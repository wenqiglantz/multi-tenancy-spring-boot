package com.github.wenqiglantz.service.customerservice.config.multitenancy;

import org.springframework.jdbc.datasource.ConnectionProxy;
import org.springframework.jdbc.datasource.DelegatingDataSource;
import org.springframework.lang.Nullable;

import javax.sql.DataSource;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

import static com.github.wenqiglantz.service.customerservice.config.multitenancy.TenantConstants.ZERO;

/**
 * Tenant-Aware Datasource that decorates Connections with current tenant information.
 */
public class TenantAwareDataSource extends DelegatingDataSource {

    public TenantAwareDataSource(DataSource targetDataSource) {
        super(targetDataSource);
    }

    @Override
    public Connection getConnection() throws SQLException {
        final Connection connection = getTargetDataSource().getConnection();
        setTenantId(connection);
        return getTenantAwareConnectionProxy(connection);
    }

    @Override
    public Connection getConnection(String username, String password) throws SQLException {
        final Connection connection = getTargetDataSource().getConnection(username, password);
        setTenantId(connection);
        return getTenantAwareConnectionProxy(connection);
    }

    // Every time the app asks the data source for a connection, set the PostgreSQL session
    // variable to the tenant id to enforce data isolation.
    private void setTenantId(Connection connection) throws SQLException {
        try (Statement sql = connection.createStatement()) {
            String tenantId = TenantContext.getTenantId();
            tenantId = null == tenantId ? ZERO : tenantId;
            sql.execute("SET app.tenant_id TO '" + tenantId + "'");
        }
    }

    // When the connection is closed, clear tenant id from PostgreSQL session
    private void clearTenantId(Connection connection) throws SQLException {
        try (Statement sql = connection.createStatement()) {
            sql.execute("RESET app.tenant_id");
        }
    }

    // Connection Proxy that intercepts close() to reset the tenant_id
    protected Connection getTenantAwareConnectionProxy(Connection connection) {
        return (Connection) Proxy.newProxyInstance(
                ConnectionProxy.class.getClassLoader(),
                new Class[] {ConnectionProxy.class},
                new TenantAwareInvocationHandler(connection));
    }

    // Connection Proxy invocation handler that intercepts close() to reset the tenant_id
    private class TenantAwareInvocationHandler implements InvocationHandler {
        private final Connection target;

        TenantAwareInvocationHandler(Connection target) {
            this.target = target;
        }

        @Nullable
        public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
            switch (method.getName()) {
                case "equals":
                    return proxy == args[0];
                case "hashCode":
                    return System.identityHashCode(proxy);
                case "toString":
                    return "Tenant-aware proxy for target Connection [" + this.target.toString() + "]";
                case "unwrap":
                    if (((Class) args[0]).isInstance(proxy)) {
                        return proxy;
                    } else {
                        return method.invoke(target, args);
                    }
                case "isWrapperFor":
                    if (((Class) args[0]).isInstance(proxy)) {
                        return true;
                    } else {
                        return method.invoke(target, args);
                    }
                case "getTargetConnection":
                    return target;
                default:
                    if (method.getName().equals("close")) {
                        clearTenantId(target);
                    }
                    return method.invoke(target, args);
            }
        }
    }
}
