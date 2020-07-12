package com.s3groupinc.gateway.api.repo;

import com.s3groupinc.gateway.api.entity.Groups;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * This represents repository for Groups entity.
 *
 * @author : Anghulakshmi B
 * @version : 1.0
 * @since : 2020-02-10
 */
@Repository
public interface GroupsRepository extends JpaRepository<Groups, Integer> {
    @Query(value = "SELECT * FROM groups WHERE group_name ILIKE %?1%", nativeQuery = true)
    Optional<Groups> findOneByGroupName(String groupName);

    List<Groups> findByGroupName(String groupName);

    @Query(value = "SELECT * FROM groups WHERE users_id is NULL AND group_name ILIKE %?1%", nativeQuery = true)
    Optional<Groups> findGroups(String groupName);
}
