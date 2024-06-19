package kr.or.argos.domain.user.presentation;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.tags.Tags;
import jakarta.servlet.http.HttpServletRequest;
import kr.or.argos.domain.user.dto.UserLogin;
import kr.or.argos.domain.user.dto.UserRegistration;
import kr.or.argos.domain.user.dto.UserUpdate;
import kr.or.argos.domain.user.entity.User;
import kr.or.argos.domain.user.service.UserService;
import kr.or.argos.security.dto.Jwt;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
@Tag(name = "User Controller", description = "User API")
public class UserController {

  private final UserService userService;

  @PostMapping("/register")
  @Operation(summary = "Register a new user", description = "Register a new user to the system")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Register successful", content = {
          @Content(mediaType = "plain/text", schema = @Schema(implementation = Jwt.class))}),
      @ApiResponse(responseCode = "400", description = "Bad Request", content = @Content)})
  @Tags({@Tag(name = "Create")})
  public ResponseEntity<String> register(@RequestBody UserRegistration request) {
    return ResponseEntity.ok(userService.register(request).toString());
  }

  @PostMapping("/login")
  @Operation(summary = "Login to the system", description = "Login to the system and get a json web token")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Login successful", content = {
          @Content(mediaType = "plain/text", schema = @Schema(implementation = Jwt.class))}),
      @ApiResponse(responseCode = "403", description = "Forbidden", content = @Content)})
  @Tags({@Tag(name = "Read")})
  public ResponseEntity<String> login(@RequestBody UserLogin request) {
    return ResponseEntity.ok(userService.login(request).toString());
  }

  @PutMapping("/update")
  @Operation(summary = "Update user information", description = "Update user information in the system")
  @SecurityRequirement(name = "bearerAuth")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Update user information successful", content = {
          @Content(mediaType = "application/json", schema = @Schema(implementation = User.class))}),
      @ApiResponse(responseCode = "403", description = "Forbidden", content = @Content)})
  @Tags({@Tag(name = "Update")})
  public ResponseEntity<User> update(HttpServletRequest servlet, @RequestBody UserUpdate request) {
    return ResponseEntity.ok(userService.update(servlet, request));
  }

  @GetMapping("/me")
  @Operation(summary = "Get user information", description = "Get user information from the system")
  @SecurityRequirement(name = "bearerAuth")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Get user information successful", content = {
          @Content(mediaType = "application/json", schema = @Schema(implementation = User.class))}),
      @ApiResponse(responseCode = "403", description = "Forbidden", content = @Content)})
  @Tags({@Tag(name = "Read")})
  public ResponseEntity<User> me(HttpServletRequest servlet) {
    return ResponseEntity.ok(userService.me(servlet));
  }

  @GetMapping("/refresh")
  @Operation(summary = "Refresh JWT", description = "Refresh json web token for the user")
  @SecurityRequirement(name = "bearerAuth")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Refresh token successful", content = {
          @Content(mediaType = "plain/text", schema = @Schema(implementation = Jwt.class))}),
      @ApiResponse(responseCode = "403", description = "Forbidden", content = @Content)})
  @Tags({@Tag(name = "Read")})
  public ResponseEntity<String> refresh(HttpServletRequest servlet) {
    return ResponseEntity.ok(userService.refresh(servlet.getRemoteUser()).toString());
  }

  @DeleteMapping("/{username}")
  @Operation(summary = "Delete user information", description = "Delete user information from the system")
  @SecurityRequirement(name = "bearerAuth")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "204", description = "Delete user information successful", content = @Content),
      @ApiResponse(responseCode = "403", description = "Forbidden", content = @Content)})
  @Tags({@Tag(name = "Delete")})
  public ResponseEntity<Void> delete(HttpServletRequest servlet, @PathVariable String username) {
    userService.delete(servlet, username);
    return ResponseEntity.noContent().build();
  }
}