package com.whizspider.gateway.api.services;

import com.whizspider.gateway.api.errors.RoleAlreadyExists;
import com.whizspider.gateway.api.errors.RoleNotFound;
import com.whizspider.gateway.api.model.Role;
import com.whizspider.gateway.api.repo.RoleRepository;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class RoleService {
  @Value("${role.already.exists}")
  private String roleAlreadyExists;

  @Value("${role.not.found}")
  private String RoleNotFound;

  @Autowired
  private RoleRepository roleRepository;

  public Page<Role> getAll(Integer pageNo, Integer pageSize,
                           String sortBy, String orderBy) {
    Pageable pageable = orderBy.equals("A") ? PageRequest.of(pageNo, pageSize
        , Sort.by(sortBy).ascending())
        : PageRequest.of(pageNo, pageSize, Sort.by(sortBy).descending());
    return roleRepository.findAll(pageable);
  }

  public Role add(Role Role) throws RoleAlreadyExists {
    String name = Role.getName();
    Optional<Role> optionalRole = roleRepository.findById(name);

    if (optionalRole.isPresent()) {
      throw new RoleAlreadyExists(roleAlreadyExists);
    }
    return roleRepository.save(Role);
  }

  public void delete(String name) throws RoleNotFound {
    Optional<Role> optionalRole = roleRepository.findById(name);

    if (!optionalRole.isPresent()) {
      throw new RoleNotFound(RoleNotFound);
    }
    roleRepository.delete(optionalRole.get());
  }
}
