package com.s3groupinc.gateway.api.repo;

import com.s3groupinc.gateway.api.entity.Groups;
import com.s3groupinc.gateway.api.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * This represents repository for Role entity.
 *
 * @author : Anghulakshmi B
 * @version : 1.0
 * @since : 2020-02-10
 */
@Repository
public interface RoleRepository extends JpaRepository<Role, Integer> {
    @Query(value = "SELECT * FROM role WHERE name ILIKE %?1%", nativeQuery = true)
    Optional<Role> findByName(String name);

    List<Role> findOneByName(String name);
}