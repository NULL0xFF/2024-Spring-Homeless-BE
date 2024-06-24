package kr.or.argos.domain.user.presentation;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import kr.or.argos.domain.user.dto.UserRegistration;
import kr.or.argos.domain.user.dto.UserUpdate;
import kr.or.argos.domain.user.entity.User;
import kr.or.argos.domain.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/admin/users")
@RequiredArgsConstructor
@Tag(name = "User Admin Controller", description = "User Administration API")
public class UserAdminController {

  private final UserService userService;

  @PostMapping("/register")
  @Operation(summary = "Register a new user", description = "Admin endpoint to register a new user in the system. Takes a UserRegistration object as input and returns a simple OK message if the registration is successful.")
  @SecurityRequirement(name = "bearerAuth")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Register successful", content = @Content),
      @ApiResponse(responseCode = "400", description = "Bad Request", content = @Content)})
  public ResponseEntity<String> registerUser(@RequestBody UserRegistration request) {
    userService.registerUser(request);
    return ResponseEntity.ok("User registered successfully");
  }

  @GetMapping("/details/{username}")
  @Operation(summary = "Get user by username", description = "Admin endpoint to retrieve user details by username.")
  @SecurityRequirement(name = "bearerAuth")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Get user information successful", content = {
          @Content(mediaType = "application/json", schema = @Schema(implementation = User.class))}),
      @ApiResponse(responseCode = "403", description = "Forbidden", content = @Content)})
  public ResponseEntity<User> getUserDetails(@PathVariable String username) {
    return ResponseEntity.ok(userService.getUser(username));
  }

  @PatchMapping("/update")
  @Operation(summary = "Update user information", description = "Admin endpoint to update the information of a user. Takes a UserUpdate object as input.")
  @SecurityRequirement(name = "bearerAuth")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Update user information successful", content = {
          @Content(mediaType = "application/json", schema = @Schema(implementation = User.class))}),
      @ApiResponse(responseCode = "403", description = "Forbidden", content = @Content)})
  public ResponseEntity<User> updateUser(@RequestBody UserUpdate request) {
    return ResponseEntity.ok(userService.updateUserByAdmin(request));
  }

  @DeleteMapping("/resign/{username}")
  @Operation(summary = "Resign user", description = "Admin endpoint to resign a user identified by username. Requires the JWT in the header for authentication.")
  @SecurityRequirement(name = "bearerAuth")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Resign successful", content = @Content),
      @ApiResponse(responseCode = "403", description = "Forbidden", content = @Content)})
  public ResponseEntity<Void> resignUser(@PathVariable String username) {
    userService.resignUserByAdmin(username);
    return ResponseEntity.ok().build();
  }
}