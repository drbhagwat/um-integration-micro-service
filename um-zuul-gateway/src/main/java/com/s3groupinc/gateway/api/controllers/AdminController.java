package com.s3groupinc.gateway.api.controllers;

import com.s3groupinc.gateway.api.email.EmailService;
import com.s3groupinc.gateway.api.entity.apidata.JsonAdminCreate;
import com.s3groupinc.gateway.api.services.AdminService;
import com.s3groupinc.gateway.api.services.UserService;
import java.util.ArrayList;
import java.util.List;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.CrossOrigin;

/**
 * Manages CRUD (Create Read Update Delete) operations of um admin.
 *
 * @author : Dinesh Bhagwat
 * @version : 1.0
 * @since : 2019-04-15
 */

@CrossOrigin(origins = "*", allowedHeaders="*")
@RestController
public class AdminController {
  @Value("${ADMIN_ADDED}")
  private String adminAdded;

  @Value("${ADMIN_UPDATED}")
  private String adminUpdated;

  @Value("${ADMIN_DISABLED}")
  private String adminDisabled;

  @Autowired
  private AdminService adminService;

  @Autowired
  private UserService userService;

  @Autowired
  private EmailService emailService;

  @Value("${registrationSubject}")
  private String registrationSubject;

  @Autowired
  private Environment env;

//  /**
//   * Returns first page of ActiveAdmins found
//   *
//   * @param pageNo   - default is 0 - can be overridden.
//   * @param pageSize - default is 10 - can be overridden.
//   * @param sortBy   - default is descending - can be overridden.
//   * @param orderBy  - default is by last_updated_date_time - can be overridden.
//   * @return - first page of the ActiveAdmins found.
//   */
//  @GetMapping("/admins")
//  public Page<Users> getAllActiveAdmins(@RequestParam(defaultValue = "0") Integer pageNo,
//                                       @RequestParam(defaultValue = "10") Integer pageSize,
//                                       @RequestParam(defaultValue = "last_updated_date_time") String sortBy,
//                                       @RequestParam(defaultValue = "A") String orderBy) {
//    return adminService.getAllActiveAdmins(pageNo, pageSize, sortBy, orderBy);
//  }

  /**
   * Gets the admin given the Primary Key(PK).
   *
   * @param id - PK of the admin
   * @return - on success, returns the admin found.
   * @throws Exception - on failure, a global exception handler is called
   *                   which displays an appropriate error message.
   */
//  @GetMapping("/admins/{id}")
//  public Users getAdmin(@PathVariable int id) throws Exception {
//    return userService.findUser(id);
//  }

  /**
   * Creates a new admin
   *
   * @param jsonAdminCreate - json input containing the data
   * @return -   on success, returns the success message.
   * @throws Exception - on failure, a global exception handler is called
   *                   which displays an appropriate error message.
   */
  @PostMapping("/admins")
  public ResponseEntity<String> addAdmin(@RequestBody @Valid JsonAdminCreate jsonAdminCreate) throws Exception {
    String createdPassword = adminService.addAdmin(jsonAdminCreate);
    ArrayList<String> emailList = new ArrayList<>();
    emailList.add(jsonAdminCreate.getEmail());
    List<String> paramsToBeReplaced = new ArrayList<>();
    String adminName = jsonAdminCreate.getAdminName();
    paramsToBeReplaced.add(adminName);
    paramsToBeReplaced.add(createdPassword);
    //emailService.sendMail(env.getProperty("CONF_DIR") + "/addAdmin.txt", paramsToBeReplaced, registrationSubject, emailList);
    return ResponseEntity.ok(adminAdded);
  }

//  /**
//   * Updates an existing admin
//   *
//   * @param id              - PK of the admin
//   * @param jsonAdminUpdate - jsonAdminUpdate is teh new data for the existing admin
//   * @return -   on success, returns the success message.
//   * @throws Exception - on failure, a global exception handler is called
//   *                   which displays an appropriate error message.
//   */
//  @PutMapping("/admins/{id}")
//  public ResponseEntity<String> updateAdmin(@PathVariable int id, @RequestBody @Valid JsonAdminUpdate jsonAdminUpdate) throws Exception {
//    adminService.updateAdmin(id, jsonAdminUpdate);
//    String adminName = jsonAdminUpdate.getAdminName();
//    ArrayList<String> emailList = new ArrayList<>();
//    emailList.add(jsonAdminUpdate.getEmail());
//    List<String> paramsToBeReplaced = new ArrayList<>();
//    paramsToBeReplaced.add(adminName);
//    emailService.sendMail(env.getProperty("CONF_DIR") + "/updateAdmin.html", paramsToBeReplaced, updateSubject, emailList);
//    return ResponseEntity.ok(adminUpdated);
//  }

  /**
   * Disables (i.e. soft delete) an existing admin.
   *
   * @param id - PK of the admin
   * @return -   on success, returns the success message.
   * @throws Exception - on failure, a global exception handler is called
   *                   which displays an appropriate error message.
   */
//  @DeleteMapping("/admins/{id}")
//  public ResponseEntity<String> deleteAdmin(@PathVariable int id) throws Exception {
//    adminService.disableAdmin(id);
//    return ResponseEntity.ok(adminDisabled);
//  }
}