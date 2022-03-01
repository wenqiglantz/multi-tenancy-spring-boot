package com.github.wenqiglantz.service.customerservice.persistence.entity;

import com.github.wenqiglantz.service.customerservice.config.multitenancy.TenantAware;
import com.github.wenqiglantz.service.customerservice.config.multitenancy.TenantListener;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.EntityListeners;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Version;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.UUID;

import static java.time.LocalDateTime.now;

@Getter
@Setter
@NoArgsConstructor
@MappedSuperclass
@EntityListeners(TenantListener.class)
public abstract class BaseEntity implements TenantAware, Serializable {

    @Column(name = "TENANT_ID")
    private String tenantId;

    @Id
    @Column(name = "ID")
    private String id;

    @Version
    @Column(name = "VERSION")
    private Long version;

    @Column(name = "INSERTED_AT")
    private LocalDateTime insertedAt;

    @Column(name = "INSERTED_BY")
    private String insertedBy;

    @Column(name = "UPDATED_AT")
    private LocalDateTime updatedAt;

    @Column(name = "UPDATED_BY")
    private String updatedBy;

    public BaseEntity(String tenantId) {
        this.tenantId = tenantId;
    }

    @Override
    public boolean equals(Object o) {
        throw new UnsupportedOperationException("Should be implemented by subclass.");
    }

    @Override
    public int hashCode() {
        throw new UnsupportedOperationException("Should be implemented by subclass.");
    }

    @PrePersist
    private void onPrePersist() {
        id = UUID.randomUUID().toString();
        insertedAt = now();
        insertedBy = "System";
        updatedAt = insertedAt;
        updatedBy = insertedBy;
    }

    @PreUpdate
    private void onPreUpdate() {
        updatedAt = now();
        updatedBy = "System";
    }
}
