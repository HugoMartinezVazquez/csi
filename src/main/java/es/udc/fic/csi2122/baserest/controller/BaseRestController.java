package es.udc.fic.csi2122.baserest.controller;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import es.udc.fic.csi2122.baserest.conversors.UserConversors;
import es.udc.fic.csi2122.baserest.dto.UserDto;
import es.udc.fic.csi2122.baserest.entity.User;
import es.udc.fic.csi2122.baserest.repository.UserRepository;

/**
 * Example spring REST controller
 * 
 * The {@link RequestMapping} annotation indicates the base path for all
 * petitions that this
 * controller will handle. the path for the endpoints defined is appended to
 * this path
 * 
 * @author alfonso.landin@udc.es
 */
@RestController
@Transactional
@RequestMapping("user")
public class BaseRestController {

  private static final Logger logger = LoggerFactory.getLogger(BaseRestController.class);

  @PersistenceContext
  private EntityManager em;

  private UserRepository userRepository;

  /**
   * Constructor dependency injection signaled by the {@link Autowired} annotation
   * 
   * @param userRepository the user repository
   */
  @Autowired
  public BaseRestController(UserRepository userRepository) {
    this.userRepository = userRepository;
  }

  /**
   * Get all users
   * 
   * @return a list of all the users
   */
  @GetMapping(value = "all")
  public List<UserDto> getUsers() {
    return UserConversors.toUserDtoList(userRepository.findAll());
  }

  /**
   * Get user by id
   * 
   * The ResponseEntity.of static method will return a 404 response if the
   * Optional&lt;User&gt; is empty.
   * 
   * @param id the id of the user
   * @return the user or not found
   */
  @GetMapping(value = "{id}")
  public ResponseEntity<UserDto> get(@PathVariable Long id) {
    logger.info("Fetching user with id: {}", id);
    var user = userRepository.findById(id);

    return ResponseEntity.of(user.map(u -> {
      logger.info("Found user with id {}: {}", id, u);
      return UserConversors.toUserDto(u);
    }));
  }

  /**
   * Create a new user
   * 
   * We use the JPA EntityManager to do the operation. Errors during this
   * operation are not handled and will results in an exceptions thrown and a 500
   * Internal Server Error response
   * 
   * Alternatively we could use the save() method of the UserRepository.
   * 
   * @param user the new user
   * @return the id of the new user
   */
  @PostMapping(value = "new")
  public Long newUser(@RequestBody UserDto user) {
    logger.info("Creating new user: {}", user);
    var newUser = em.merge(UserConversors.toUser(user));
    logger.info("Created user: {}", newUser);
    return newUser.getId();
  }

  /**
   * Search users
   * 
   * Parameters are optional, but at least one is required. sIf no query parameter
   * is provided a 400 Bad Response is issued.
   * 
   * @param name      the name of the user
   * @param olderThan the minimum age
   * @return a list of users
   */
  @GetMapping(value = "search")
  public ResponseEntity<List<UserDto>> search(@RequestParam Optional<String> name,
      @RequestParam(name = "older-than") Optional<Integer> olderThan) {

    final List<User> users;

    if (name.isEmpty() && olderThan.isEmpty()) {
      return ResponseEntity.badRequest().build();
    } else if (name.isPresent() && olderThan.isEmpty()) {
      users = userRepository.findOneByName(name.get())
          .map(Arrays::asList)
          .orElseGet(Arrays::asList);
    } else if (name.isEmpty()) {
      users = userRepository.findByAgeGreaterThan(olderThan.get());
    } else {
      users = userRepository.findByNameAndAgeGreaterThan(name.get(), olderThan.get());
    }

    return ResponseEntity.ok(UserConversors.toUserDtoList(users));
  }
}
