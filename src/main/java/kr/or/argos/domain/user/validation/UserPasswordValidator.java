package kr.or.argos.domain.user.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import java.util.regex.Pattern;

/**
 * Check that a character sequence is not {@code null} and meets the password requirements.
 *
 * @author Myoung Ha, Ji
 */
public class UserPasswordValidator implements
    ConstraintValidator<Password, CharSequence> {

  /**
   * Checks that the character sequence is not {@code null} and meets the password requirements.
   *
   * @param password the character sequence to validate
   * @param context  context in which the constraint is evaluated
   * @return returns {@code true} if the string is not {@code null} and {@code password} is at least
   * 12 characters long and includes an uppercase letter, a lowercase letter, a digit, and a special
   * character, otherwise {@code false}
   */
  @Override
  public boolean isValid(CharSequence password, ConstraintValidatorContext context) {
    if (password == null) {
      return false;
    }
    Pattern passwordPattern = Pattern.compile(
        "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{12,}$");
    return passwordPattern.matcher(password).matches();
  }
}