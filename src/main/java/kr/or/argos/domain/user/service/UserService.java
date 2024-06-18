package kr.or.argos.domain.user.service;

import jakarta.servlet.http.HttpServletRequest;
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

  public Jwt register(UserRegistration request) {
    // 사용자명 중복 확인
    if (userRepository.findByUsername(request.getUsername()).isPresent()) {
      throw new InvalidRequestException("username already exists");
    }    // 요청으로부터 사용자 생성
    User user = userRepository.save(request.toEntity(encoder));
    // 기본 그룹 추가
    UserGroup userGroup = UserGroup.builder().user(user).group(groupService.getUserGroup()).build();
    user.getUserGroups().add(userGroup);
    // 사용자 저장 후 토큰 생성
    return tokenProvider.createToken(userRepository.save(user));
  }

  @Transactional(readOnly = true)
  public Jwt login(UserLogin request) {
    authenticationManager.authenticate(
        new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword()));
    return tokenProvider.createToken(userDetailsService.loadUserByUsername(request.getUsername()));
  }

  public User update(HttpServletRequest servlet, UserUpdate info) {
    User user = (User) userDetailsService.loadUserByUsername(
        tokenProvider.getUsername(tokenProvider.resolveToken(servlet)));
    if (user.getUsername().equals(info.getUsername())) {
      user.setPassword(encoder.encode(info.getPassword()));
    } else {
      throw new InvalidRequestException("Username does not match");
    }
    return userRepository.save(user);
  }

  @Transactional(readOnly = true)
  public Jwt refresh(String username) {
    return tokenProvider.createToken(userDetailsService.loadUserByUsername(username));
  }

  @Transactional(readOnly = true)
  public User me(HttpServletRequest servlet) {
    return (User) userDetailsService.loadUserByUsername(
        tokenProvider.getUsername(tokenProvider.resolveToken(servlet)));
  }

  public void delete(HttpServletRequest servlet, String username) {
    User user = (User) userDetailsService.loadUserByUsername(
        tokenProvider.getUsername(tokenProvider.resolveToken(servlet)));
    if (user.getUsername().equals(username)) {
      // 자가 삭제
      userRepository.delete(user);
    } else if (user.getAuthorities().contains(groupService.getAdminGroup())) {
      // 관리자 권한으로 삭제
      User targetUser = userRepository.findByUsername(username)
          .orElseThrow(() -> new InvalidRequestException("Username not found"));
      userRepository.delete(targetUser);
    } else {
      throw new ForbiddenException("You are not authorized to delete this user");
    }
  }
}