package kr.or.argos.domain.user.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * The annotated element must not be {@code null} and must be a valid student ID. The value must be
 * a number between 100,000,000 and 999,999,999. Accepts {@code Integer}.
 *
 * @author Myoung Ha, Ji
 */
@Documented
@Constraint(validatedBy = UserStudentIdValidator.class)
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface StudentId {

  String message() default "Student ID must be 9 digits.";

  Class<?>[] groups() default {};

  Class<? extends Payload>[] payload() default {};
}