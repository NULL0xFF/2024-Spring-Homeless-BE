package kr.or.argos.security.config;

import kr.or.argos.security.filter.JwtRequestFilter;
import kr.or.argos.security.service.TokenService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.SecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.DefaultSecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@RequiredArgsConstructor
public class JwtConfig extends
    SecurityConfigurerAdapter<DefaultSecurityFilterChain, HttpSecurity> {

  private final TokenService tokenService;

  @Override
  public void configure(HttpSecurity http) {
    JwtRequestFilter customFilter = new JwtRequestFilter(tokenService);
    http.addFilterBefore(customFilter, UsernamePasswordAuthenticationFilter.class);
  }
}