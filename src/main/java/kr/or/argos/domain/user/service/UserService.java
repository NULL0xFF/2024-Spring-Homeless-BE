package kr.or.argos.domain.user.service;

import jakarta.servlet.http.HttpServletRequest;
import kr.or.argos.domain.user.dto.UserDeletion;
import kr.or.argos.domain.user.dto.UserLogin;
import kr.or.argos.domain.user.dto.UserRegistration;
import kr.or.argos.domain.user.dto.UserUpdate;
import kr.or.argos.domain.user.entity.User;
import kr.or.argos.domain.user.entity.UserGroup;
import kr.or.argos.domain.user.repository.UserRepository;
import kr.or.argos.global.exception.ForbiddenException;
import kr.or.argos.global.exception.InvalidRequestException;
import kr.or.argos.security.dto.Jwt;
import kr.or.argos.security.provider.JwtProvider;
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
  private final JwtProvider tokenProvider;
  private final PasswordEncoder encoder;

  public Jwt registerUser(UserRegistration request) {
    // Check if the username already exists
    if (userRepository.findByUsername(request.getUsername()).isPresent()) {
      throw new InvalidRequestException("Username already exists");
    }
    // Create a new user from the registration request
    User user = userRepository.save(request.toEntity(encoder));
    // Add the user to the default group
    UserGroup userGroup = UserGroup.builder().user(user).group(groupService.getUserGroup()).build();
    user.getUserGroups().add(userGroup);
    // Save the user and generate a JWT
    return tokenProvider.createToken(userRepository.save(user));
  }

  @Transactional
  public Jwt loginUser(UserLogin request) {
    authenticationManager.authenticate(
        new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword()));
    return tokenProvider.createToken(userDetailsService.loadUserByUsername(request.getUsername()));
  }

  public User updateUser(HttpServletRequest servlet, UserUpdate info) {
    User user = (User) userDetailsService.loadUserByUsername(
        tokenProvider.getUsername(tokenProvider.resolveToken(servlet)));
    if (user.getUsername().equals(info.getUsername())) {
      user.setPassword(encoder.encode(info.getPassword()));
    } else {
      throw new InvalidRequestException("Username does not match");
    }
    return userRepository.save(user);
  }

  @Transactional
  public Jwt refreshJwt(String username) {
    return tokenProvider.createToken(userDetailsService.loadUserByUsername(username));
  }

  @Transactional(readOnly = true)
  public User getUserDetails(HttpServletRequest servlet) {
    return (User) userDetailsService.loadUserByUsername(
        tokenProvider.getUsername(tokenProvider.resolveToken(servlet)));
  }

  public void resignUser(HttpServletRequest servlet, UserDeletion request) {
    User user = (User) userDetailsService.loadUserByUsername(
        tokenProvider.getUsername(tokenProvider.resolveToken(servlet)));
    if (!user.getUsername().equals(request.getUsername()) || !encoder.matches(request.getPassword(),
        user.getPassword())) {
      throw new InvalidRequestException("Invalid username or password");
    }
    userRepository.delete(user);
  }

  public void resignUserByAdmin(HttpServletRequest servlet, String username) {
    User admin = (User) userDetailsService.loadUserByUsername(
        tokenProvider.getUsername(tokenProvider.resolveToken(servlet)));
    if (admin.getAuthorities().contains(groupService.getAdminGroup())) {
      User targetUser = userRepository.findByUsername(username)
          .orElseThrow(() -> new InvalidRequestException("Username not found"));
      userRepository.delete(targetUser);
    } else {
      throw new ForbiddenException("You are not authorized to resign this user");
    }
  }

  public User getUserByUsername(HttpServletRequest servlet, String username) {
    return userRepository.findByUsername(username)
        .orElseThrow(() -> new InvalidRequestException("Username not found"));
  }

  public User updateUserByAdmin(UserUpdate request) {
    User user = userRepository.findByUsername(request.getUsername())
        .orElseThrow(() -> new InvalidRequestException("Username not found"));
    user.setPassword(encoder.encode(request.getPassword()));
    return userRepository.save(user);
  }
}