package com.s3groupinc.gateway.api.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * This class represents Permissions entity
 *
 * @author : Anghulakshmi B
 * @version : 1.0
 * @since : 2020-02-10
 */

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Permissions {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(updatable = false, nullable = false)
    private Integer id;

    private String permissionName;
    private String permissionValue;
    private Integer groupsId;
}