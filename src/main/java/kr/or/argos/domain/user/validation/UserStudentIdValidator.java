package kr.or.argos.domain.user.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

/**
 * Check that a character sequence is not {@code null} and is a valid student ID number.
 *
 * @author Myoung Ha, Ji
 */
public class UserStudentIdValidator implements
    ConstraintValidator<StudentId, Integer> {

  /**
   * Checks that the number is not {@code null} and is a valid student ID number.
   *
   * @param studentId the integer to validate
   * @param context   context in which the constraint is evaluated
   * @return returns {@code true} if the number is not {@code null} and {@code studentId} is a
   * 9-digit number, otherwise {@code false}
   */
  @Override
  public boolean isValid(Integer studentId, ConstraintValidatorContext context) {
    if (studentId == null) {
      return false;
    }
    return studentId >= 100_000_000 && studentId <= 999_999_999;
  }
}