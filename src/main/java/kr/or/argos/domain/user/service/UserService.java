package kr.or.argos.domain.user.service;

import kr.or.argos.domain.user.dto.UserDeletion;
import kr.or.argos.domain.user.dto.UserLogin;
import kr.or.argos.domain.user.dto.UserRegistration;
import kr.or.argos.domain.user.dto.UserUpdate;
import kr.or.argos.domain.user.entity.User;
import kr.or.argos.domain.user.entity.UserGroup;
import kr.or.argos.domain.user.repository.UserRepository;
import kr.or.argos.global.exception.InvalidRequestException;
import kr.or.argos.security.entity.Token;
import kr.or.argos.security.service.TokenService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class UserService {

  private final UserDetailsService userDetailsService;
  private final UserRepository userRepository;
  private final GroupService groupService;
  private final AuthenticationManager authenticationManager;
  private final TokenService tokenService;
  private final PasswordEncoder encoder;

  @Transactional
  public Token registerUser(UserRegistration request) {
    // Check if the username already exists
    if (userRepository.existsByUsername(request.getUsername())) {
      throw new InvalidRequestException("Username already exists");
    }

    // Create a new user from the registration request
    User user = userRepository.save(request.toEntity(encoder));

    // Add the user to the default group
    UserGroup userGroup = UserGroup.builder().user(user).group(groupService.getUserGroup()).build();
    user.getUserGroups().add(userGroup);

    // Save the user and generate a token
    return tokenService.issueToken(userRepository.save(user));
  }

  @Transactional
  public Token loginUser(UserLogin request) {
    authenticationManager.authenticate(
        new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword()));
    return tokenService.issueToken(userDetailsService.loadUserByUsername(request.getUsername()));
  }

  @Transactional
  public Token refreshToken(String username) {
    // Re-issue a token for the user
    return tokenService.issueToken(userDetailsService.loadUserByUsername(username));
  }

  @Transactional(readOnly = true)
  public User getUser(String username) {
    return userRepository.findByUsername(username)
        .orElseThrow(() -> new InvalidRequestException("Username not found"));
  }

  @Transactional
  public User updateUser(String remoteUsername, UserUpdate request) {
    // Get requester user details
    User user = getUser(remoteUsername);

    // Check if the username matches
    if (user.getUsername().equals(request.getUsername())) {
      // Update user entity with non-null variables
      request.updateEntity(user, encoder);
    } else {
      // If not, throw an exception
      throw new InvalidRequestException("Username does not match");
    }

    // Save the updated user
    return userRepository.save(user);
  }

  @Transactional
  public User updateUserByAdmin(UserUpdate request) {
    User user = getUser(request.getUsername());
    request.updateEntity(user, encoder);
    return userRepository.save(user);
  }

  @Transactional
  public void resignUser(String remoteUsername, UserDeletion request) {
    // Get requester user details
    User user = getUser(remoteUsername);

    // Check if requester is the user to be deleted
    if (!user.getUsername().equals(request.getUsername()) || !encoder.matches(request.getPassword(),
        user.getPassword())) {
      // If not, throw an exception
      throw new InvalidRequestException("Invalid username or password");
    }

    // If the requester is the user to be deleted, delete the user
    userRepository.delete(user);
  }

  @Transactional
  public void resignUserByAdmin(String username) {
    userRepository.delete(getUser(username));
  }
}