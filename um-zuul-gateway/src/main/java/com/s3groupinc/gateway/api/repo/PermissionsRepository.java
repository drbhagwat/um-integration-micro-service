package com.s3groupinc.gateway.api.repo;

import com.s3groupinc.gateway.api.entity.Permissions;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

/**
 * This represents repository for Permission entity.
 *
 * @author : Anghulakshmi B
 * @version : 1.0
 * @since : 2020-02-10
 */
@Repository
public interface PermissionsRepository extends JpaRepository<Permissions, Integer> {
    @Query(value = "SELECT * FROM permissions WHERE permission_name ILIKE %?1%", nativeQuery = true)
    Optional<Permissions> findByPermissionName(String permissionName);

    List<Permissions> findOneByPermissionName(String permissionName);
}