package com.whizspider.gateway.api.services;

import com.whizspider.gateway.api.errors.UserAlreadyExists;
import com.whizspider.gateway.api.errors.UserNotFound;
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

  public User get(String name) throws UserNotFound {
    Optional<User> optionalUser = userRepository.findByName(name);

    if (optionalUser.isEmpty()) {
      throw new UserNotFound(userNotFound);
    }
    return optionalUser.get();
  }

  public User add(User user) throws UserAlreadyExists {
    try {
      get(user.getName());
      throw new UserAlreadyExists(userAlreadyExists);
    } catch (UserNotFound userNotFound) {
      return userRepository.save(user);
    }
  }

  public User update(String name, User user) throws UserNotFound {
    try {
      User existingUser = get(user.getName());
      existingUser.setFirstSuccessfulLogin(user.isFirstSuccessfulLogin());
      existingUser.setMostRecentLoginSuccessful(user.isMostRecentLoginSuccessful());
      return userRepository.save(existingUser);
    } catch (UserNotFound userNotFound) {
      throw userNotFound;
    }
  }

  public void delete(String name) throws UserNotFound {
    userRepository.delete(get(name));
  }
}
