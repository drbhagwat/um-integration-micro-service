package com.s3groupinc.gateway.api.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

/**
 * This class represents Groups entity
 *
 * @author : Anghulakshmi B
 * @version : 1.0
 * @since : 2020-02-10
 */

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Groups {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(updatable = false, nullable = false)
    private Integer id;

    private String groupName;

    private Integer usersId;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "groupsId")
    private List<Permissions> permissionsList = new ArrayList<>();

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "groupsId")
    private List<Menu> menuList = new ArrayList<>();
}