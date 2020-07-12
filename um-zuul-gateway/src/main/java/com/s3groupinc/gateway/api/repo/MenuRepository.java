package com.s3groupinc.gateway.api.repo;

import com.s3groupinc.gateway.api.entity.Menu;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

/**
 * This represents repository for Menu entity.
 *
 * @author : Sachin Kulkarni
 * @version : 1.0
 * @since : 2020-03-04
 */
@Repository
public interface MenuRepository extends JpaRepository<Menu, Integer> {
    Menu findByName(String name);

    List<Menu> findOneByName(String name);

    @Query(value = "SELECT * FROM menu WHERE menus_id is null ", nativeQuery = true)
    List<Menu> findAllParentWithChildMenus();
}