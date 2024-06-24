package kr.or.argos.domain.user.presentation;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import kr.or.argos.domain.user.dto.UserDeletion;
import kr.or.argos.domain.user.dto.UserLogin;
import kr.or.argos.domain.user.dto.UserRegistration;
import kr.or.argos.domain.user.dto.UserUpdate;
import kr.or.argos.domain.user.entity.User;
import kr.or.argos.domain.user.service.UserService;
import kr.or.argos.security.dto.TokenString;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
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
  @Operation(summary = "Register a new user", description = "Endpoint to register a new user in the system. Takes a UserRegistration object as input and returns a JWT if the registration is successful.")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Register successful", content = {
          @Content(mediaType = "plain/text", schema = @Schema(implementation = TokenString.class))}),
      @ApiResponse(responseCode = "400", description = "Bad Request", content = @Content)})
  public ResponseEntity<String> registerUser(@RequestBody UserRegistration request) {
    return ResponseEntity.ok(userService.registerUser(request).toTokenString().toString());
  }

  @PostMapping("/login")
  @Operation(summary = "Login to the system", description = "Endpoint for users to log in. Takes a UserLogin object as input and returns a JWT if authentication is successful.")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Login successful", content = {
          @Content(mediaType = "plain/text", schema = @Schema(implementation = TokenString.class))}),
      @ApiResponse(responseCode = "403", description = "Forbidden", content = @Content)})
  public ResponseEntity<String> loginUser(@RequestBody UserLogin request) {
    return ResponseEntity.ok(userService.loginUser(request).toTokenString().toString());
  }

  @GetMapping("/refresh")
  @Operation(summary = "Refresh JWT", description = "Endpoint to refresh the JSON Web Token for the logged-in user. Returns a new JWT.")
  @SecurityRequirement(name = "bearerAuth")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Refresh token successful", content = {
          @Content(mediaType = "plain/text", schema = @Schema(implementation = TokenString.class))}),
      @ApiResponse(responseCode = "403", description = "Forbidden", content = @Content)})
  public ResponseEntity<String> refreshToken(HttpServletRequest servlet) {
    return ResponseEntity.ok(
        userService.refreshToken(servlet.getRemoteUser()).toTokenString().toString());
  }

  @GetMapping("/me")
  @Operation(summary = "Get user information", description = "Endpoint to retrieve the information of the logged-in user.")
  @SecurityRequirement(name = "bearerAuth")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Get user information successful", content = {
          @Content(mediaType = "application/json", schema = @Schema(implementation = User.class))}),
      @ApiResponse(responseCode = "403", description = "Forbidden", content = @Content)})
  public ResponseEntity<User> getUser(HttpServletRequest servlet) {
    return ResponseEntity.ok(userService.getUser(servlet.getRemoteUser()));
  }

  @PatchMapping("/update")
  @Operation(summary = "Update user information", description = "Endpoint to update the information of the logged-in user. Takes a UserUpdate object as input.")
  @SecurityRequirement(name = "bearerAuth")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Update user information successful", content = {
          @Content(mediaType = "application/json", schema = @Schema(implementation = User.class))}),
      @ApiResponse(responseCode = "403", description = "Forbidden", content = @Content)})
  public ResponseEntity<User> updateUser(HttpServletRequest servlet,
      @RequestBody UserUpdate request) {
    return ResponseEntity.ok(userService.updateUser(servlet.getRemoteUser(), request));
  }

  @DeleteMapping("/resign")
  @Operation(summary = "Resign", description = "Endpoint for the logged-in user to resign from the system. Takes a UserDeletion object as input along with the JWT.")
  @SecurityRequirement(name = "bearerAuth")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Resign successful", content = @Content),
      @ApiResponse(responseCode = "403", description = "Forbidden", content = @Content)})
  public ResponseEntity<Void> resignUser(HttpServletRequest servlet,
      @RequestBody UserDeletion request) {
    userService.resignUser(servlet.getRemoteUser(), request);
    return ResponseEntity.ok().build();
  }
}