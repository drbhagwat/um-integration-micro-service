package com.whizspider.gateway.api.services;

import com.whizspider.gateway.api.errors.RoleNotFound;
import com.whizspider.gateway.api.errors.UserAlreadyExists;
import com.whizspider.gateway.api.model.User;
import com.whizspider.gateway.api.repo.UserRepository;
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
public class UserService {
  @Value("${user.already.exists}")
  private String userAlreadyExists;

  @Value("${user.not.found}")
  private String userNotFound;

  @Autowired
  private UserRepository userRepository;

  public Page<User> getAll(Integer pageNo, Integer pageSize,
                           String sortBy, String orderBy) {
    Pageable pageable = orderBy.equals("A") ? PageRequest.of(pageNo, pageSize
        , Sort.by(sortBy).ascending())
        : PageRequest.of(pageNo, pageSize, Sort.by(sortBy).descending());
    return userRepository.findAll(pageable);
  }

  public User add(User user) throws UserAlreadyExists {
    String name = user.getName();
    Optional<User> optionalUser = userRepository.findById(name);

    if (optionalUser.isPresent()) {
      throw new UserAlreadyExists(userAlreadyExists);
    }
    return userRepository.save(user);
  }

  public void delete(String name) throws RoleNotFound {
    Optional<User> optionalUser = userRepository.findById(name);

    if (!optionalUser.isPresent()) {
      throw new RoleNotFound(userNotFound);
    }
    userRepository.delete(optionalUser.get());
  }
}
