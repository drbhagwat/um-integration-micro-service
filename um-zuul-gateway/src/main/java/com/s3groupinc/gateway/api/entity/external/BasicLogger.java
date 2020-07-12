package com.s3groupinc.gateway.api.entity.external;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.EntityListeners;
import javax.persistence.MappedSuperclass;
import java.time.LocalDateTime;

@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
@Data
@NoArgsConstructor
@AllArgsConstructor
public abstract class BasicLogger<U> {
    @CreatedBy
    protected U createdUser;

    @CreatedDate
    protected LocalDateTime createdDateTime;

    @LastModifiedBy
    protected U lastUpdatedUser;

    @LastModifiedDate
    protected LocalDateTime lastUpdatedDateTime;
}