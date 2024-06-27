package kr.or.argos.domain.user.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.util.Date;
import kr.or.argos.domain.user.entity.User;
import kr.or.argos.domain.user.validation.Password;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.security.crypto.password.PasswordEncoder;

@Getter
@AllArgsConstructor
public class UserUpdate implements UserRequest {

  @NotNull(message = "Username must not be null")
  @NotBlank(message = "Username must not be blank")
  private String username;

  @NotNull(message = "Password must not be null")
  @Password
  private String password;

  private String email;

  private Integer studentId;

  private String surname;

  private String forename;

  private Date birthday;

  public void updateEntity(User user, PasswordEncoder encoder) {
    if (password != null) {
      user.setPassword(encoder.encode(password));
    }
    if (email != null) {
      user.setEmail(email);
    }
    if (studentId != null) {
      user.setStudentId(studentId);
    }
    if (surname != null) {
      user.setSurname(surname);
    }
    if (forename != null) {
      user.setForename(forename);
    }
    if (birthday != null) {
      user.setBirthday(birthday);
    }
  }
}