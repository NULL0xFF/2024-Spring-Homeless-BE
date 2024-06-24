package kr.or.argos.security.config;

import kr.or.argos.domain.user.service.GroupService;
import kr.or.argos.security.filter.JwtRequestFilter;
import kr.or.argos.security.service.TokenService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

  private final GroupService groupService;

  @Bean
  public PasswordEncoder passwordEncoder() {
    return PasswordEncoderFactories.createDelegatingPasswordEncoder();
  }

  @Bean
  public AuthenticationManager authenticationManagerBean(
      AuthenticationConfiguration authenticationConfiguration) throws Exception {
    return authenticationConfiguration.getAuthenticationManager();
  }

  @Bean
  public SecurityFilterChain filterChain(HttpSecurity http, TokenService tokenService)
      throws Exception {
    // @formatter:off
    http
        // Disable CSRF (cross site request forgery)
        .csrf(AbstractHttpConfigurer::disable)
        // No session will be created or used by spring security
        .sessionManagement(session ->
            session
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS))
        .authorizeHttpRequests(requests ->
            requests
                // Allow access to Swagger UI
                .requestMatchers("/v3/api-docs", "/v3/api-docs/**", "/swagger-ui/**").permitAll()
                // Allow access to user registration and login
                .requestMatchers("/users/register", "/users/login").permitAll()
                // Allow access to user APIs only for authenticated users
                .requestMatchers("/users/**").authenticated()
                // Allow access to admin APIs only for users with admin authority
                .requestMatchers("/admin/**").hasAuthority(groupService.getAdminGroup().getName())
                // Allow access to all other APIs only for authenticated users
                .anyRequest().authenticated()
        )
        .exceptionHandling(exception ->
            exception
                .accessDeniedHandler(accessDeniedHandler())
        )
        // Add JSON Web Token filter
        .addFilterBefore(new JwtRequestFilter(tokenService), UsernamePasswordAuthenticationFilter.class);
    // @formatter:on
    return http.build();
  }

  @Bean
  public AccessDeniedHandler accessDeniedHandler() {
    return (request, response, accessDeniedException) -> response.sendRedirect("/users/login");
  }
}