package com.s3groupinc.gateway.api.entity.external;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.time.LocalDateTime;

/**
 * This class represents BaseId entity with commonly used properties.
 * This class should be extended whenever needed. It provides a primary key and certain auditing
 * capabilities - who created & updated a database record and date&time of creation and update.
 *
 * @author : Dinesh Bhagwat
 * @version : 1.0
 * @since : 2019-04-15
 */
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
@Data
@NoArgsConstructor
@AllArgsConstructor
public abstract class BaseIdEntity<U> {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(updatable = false, nullable = false)
    protected int id;

    @CreatedBy
    protected U createdUser;

    @CreatedDate
    protected LocalDateTime createdDateTime;

    @LastModifiedBy
    protected U lastUpdatedUser;

    @LastModifiedDate
    protected LocalDateTime lastUpdatedDateTime;
}