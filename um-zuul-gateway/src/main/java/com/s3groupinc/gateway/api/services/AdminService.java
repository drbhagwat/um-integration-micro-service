package com.s3groupinc.gateway.api.services;

import com.s3groupinc.gateway.api.config.jpa.AuditorAwareImpl;
import com.s3groupinc.gateway.api.entity.Role;
import com.s3groupinc.gateway.api.entity.User;
import com.s3groupinc.gateway.api.entity.apidata.JsonAdminCreate;
import com.s3groupinc.gateway.api.errors.UserAlreadyExists;
import com.s3groupinc.gateway.api.repo.RoleRepository;
import com.s3groupinc.gateway.api.repo.UserRepository;
import com.s3groupinc.gateway.api.util.GenerateRandomPassword;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

/**
 * Provides CRUD services for Admin Controller.
 *
 * @author : Dinesh Bhagwat
 * @version : 1.0
 * @since : 2019-04-15
 */

@Service
@Transactional
public class AdminService {
  @Value("${ADMIN_NOT_FOUND}")
  private String adminNotFound;

  @Value("${ADMIN_NAME_ALREADY_EXISTS}")
  private String adminNameAlreadyExists;

  @Value("${ADMIN_EMAIL_ALREADY_EXISTS}")
  private String adminEmailAlreadyExists;

  @Value("${ADMIN_UPDATE_BY_DIFFERENT_USER_NOT_ALLOWED}")
  private String adminUpdateByDifferentUserNotAllowed;

  @Value("${ADMIN_NOT_DISABLED_MINIMUM_ONE_NEEDED}")
  private String adminNotDisabledMinimumOneNeeded;

  @Value("${ADMIN_NOT_UPDATED_MINIMUM_ONE_WITH_API_ACCESS_NEEDED}")
  private String adminNotUpdatedMinimumOneWithApiAccessNeeded;

  @Value("${ROLE_USER}")
  private String roleUser;

  @Value("${ROLE_ADMIN}")
  private String roleAdmin;

  @Autowired
  private UserService userService;

  @Autowired
  private UserRepository userRepository;

  @Autowired
  private PasswordEncoder passwordEncoder;

  @Autowired
  @PersistenceContext
  private EntityManager em;

  @Autowired
  private RoleRepository roleRepository;

  @Autowired
  private AuditorAwareImpl auditorAwareImpl;

  /**
   * Adds new admin
   *
   * @param jsonAdminCreate - jsonAdminCreate, which is a json class to add the
   *                        admin
   * @return - on success, returns the success message.
   * @throws Exception - on failure, a global exception handler is called which
   *                   displays an appropriate error message.
   */
  public String addAdmin(JsonAdminCreate jsonAdminCreate) throws Exception {
    String adminName = jsonAdminCreate.getAdminName();
    String fullName = jsonAdminCreate.getFullName();
    String email = jsonAdminCreate.getEmail().toLowerCase();
    boolean apiEnabled = jsonAdminCreate.getApiEnabled();
    Optional<User> user = userRepository.findOneByUsername(adminName);

    //User user = userRepository.findByFirstName(adminName);

    if (user.isPresent()) {
      throw new UserAlreadyExists(adminNameAlreadyExists);
    }
    user = userRepository.findOneByEmail(email);

    if (user.isPresent()) {
      throw new UserAlreadyExists(adminEmailAlreadyExists);
    }
    // create a new user object
    User usr = user.get();

    // save name, full name, email, and apiEnabled you got from JSON

    usr.setUsername(adminName);
    usr.setFullName(fullName);
    usr.setEmail(email);
    // enable or disable API access as specified
    usr.setApiEnabled(apiEnabled);
    // make the admin active by default.
    usr.setActive(true);
    // make this user an admin
    usr.setAdmin(true);
    // generate a random password for the new admin
    String password = GenerateRandomPassword.generatePassword();
    // encode that password so there is some security
    String encodedPassword = passwordEncoder.encode(password);
    usr.setPassword(passwordEncoder.encode(encodedPassword));

    List<Role> roles = new ArrayList<>();
    // add the admin role by default
    roles.add(new Role(roleAdmin));

    // add a role ROLE_USER only if the api enabled flag is set to true.
    if (apiEnabled) {
      // add the normal user role
      roles.add(new Role(roleUser));
    }
    usr.setRoles(roles);
    usr.setFirstLogin(true);
    // finally save the admin
    userRepository.save(usr);
    return encodedPassword;
  }

//  /**
//   * Updates an existing admin
//   *
//   * @param id              - PK f the admin
//   * @param jsonAdminUpdate - json input containing the data
//   * @return - on success, returns the success message.
//   * @throws Exception - on failure, a global exception handler is called which
//   *                   displays an appropriate error message.
//   */
//  public void updateAdmin(int id, JsonAdminUpdate jsonAdminUpdate) throws Exception {
//    // get number of active admins who have access to external apis in GIV
//    Query query1 = em.createNativeQuery(
//      "SELECT count(*) from users WHERE is_admin is TRUE AND is_active is TRUE AND " + "api_enabled is TRUE");
//    Query query2 = em.createNativeQuery(
//      "SELECT id from users WHERE is_admin is TRUE AND is_active is TRUE AND " + "api_enabled is TRUE");
//
//    Integer numberOfActiveAndApiEnabledAdmins = ((BigInteger) query1.getSingleResult()).intValue();
//    List<Integer> idsOfActiveAndApiEnabledAdmins = query2.getResultList();
//    boolean apiEnabled = jsonAdminUpdate.getApiEnabled();
//
//    if ((numberOfActiveAndApiEnabledAdmins == 1) && (idsOfActiveAndApiEnabledAdmins.size() == 1)
//      && (idsOfActiveAndApiEnabledAdmins.get(0) == id) && (!apiEnabled)) {
//      throw new Exception(adminNotUpdatedMinimumOneWithApiAccessNeeded);
//    }
//    Optional<String> loggedinUser = auditorAwareImpl.getCurrentAuditor();
//
//    if (loggedinUser.isEmpty()) {
//      throw new UserNotFound(adminNotFound);
//    }
//
//    // check if the same admin is trying to update or not?
//    if (userRepository.findOneByFirstName(loggedinUser.get()).getId() != id) {
//      throw new Exception(adminUpdateByDifferentUserNotAllowed);
//    }
//    User user = userService.findUser(id);
//
//    if (user == null) {
//      throw new UserNotFound(adminNotFound);
//    }
//    String userName = jsonAdminUpdate.getAdminName();
//    String email = jsonAdminUpdate.getEmail().toLowerCase();
//    user.setUsername(userName);
//    user.setFullName(jsonAdminUpdate.getFullName());
//    user.setEmail(email);
//    user.setApiEnabled(apiEnabled);
//    // make the admin active.
//    user.setActive(true);
//
//    // add roles ROLE_USER to admin if the api enabled flag is true.
//    if (apiEnabled) {
//      List<Role> roles = user.getRoles();
//
//      // enable external APIs only if it is not enabled so far.
//      if (roles.size() == 1) {
//        // add the normal user role
//        roles.add(new Role(roleUser));
//        user.setRoles(roles);
//      }
//    } else {
//      // disable this user from using only the external APIs
//      deleteRoles(id);
//    }
//    // save the admin to the db
//    userRepository.save(user);
//  }
//
//  /**
//   * Deletes the role ROLE_USER for a user.
//   *
//   * @param id - PK of the user
//   * @return - on success, returns the success message.
//   * @throws Exception - on failure, a global exception handler is called which
//   *                   displays an appropriate error message.
//   */
//  private void deleteRoles(int id) {
//    // get all roles for the given user id from the link table
//    Query query = em.createNativeQuery("SELECT role_id from users_role where users_id = ?");
//    query.setParameter(1, id);
//    List<Integer> userRoles = query.getResultList();
//
//    // for every role
//    for (Integer userRole : userRoles) {
//      // get the role name
//      query = em.createNativeQuery("SELECT name from role where id = ?");
//      query.setParameter(1, userRole);
//      String roleName = (String) query.getSingleResult();
//
//      if (roleName.equals(roleUser)) {
//        // delete if the current role is ROLE_USER - from the link table
//        query = em.createNativeQuery("delete from users_role where users_id = ? AND role_id = ?");
//        query.setParameter(1, id);
//        query.setParameter(2, userRole);
//        query.executeUpdate();
//
//        // delete the role from the role table
//        roleRepository.deleteByRoleId(userRole);
//      }
//    }
//  }
//
//  /**
//   * Disables an existing admin.
//   *
//   * @param id - PK of the admin
//   * @return - on success, returns the success message.
//   * @throws Exception - on failure, a global exception handler is called which
//   *                   displays an appropriate error message.
//   */
//  public void disableAdmin(int id) throws Exception {
//    // get number of active admins who have access to external apis in GIV
//    Query query1 = em.createNativeQuery(
//      "SELECT count(*) from users WHERE is_admin is TRUE AND is_active is TRUE AND " + "api_enabled is TRUE");
//    Query query2 = em.createNativeQuery(
//      "SELECT id from users WHERE is_admin is TRUE AND is_active is TRUE AND " + "api_enabled is TRUE");
//
//    Integer numberOfActiveAndApiEnabledAdmins = ((BigInteger) query1.getSingleResult()).intValue();
//    List<Integer> idsOfActiveAndApiEnabledAdmins = query2.getResultList();
//
//    if ((numberOfActiveAndApiEnabledAdmins == 1) && (idsOfActiveAndApiEnabledAdmins.size() == 1)
//      && (idsOfActiveAndApiEnabledAdmins.get(0) == id)) {
//      throw new Exception(adminNotDisabledMinimumOneNeeded);
//    }
//    Users user = userService.findUser(id);
//
//    if (user == null) {
//      throw new UserNotFound(adminNotFound);
//    }
//    deleteRoles(id);
//    // make the admin inactive.
//    user.setActive(false);
//    // save the admin to the db.
//    userRepository.save(user);
//  }
}