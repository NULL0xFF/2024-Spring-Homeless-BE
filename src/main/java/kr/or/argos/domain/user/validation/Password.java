package kr.or.argos.domain.user.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * The annotated element must not be {@code null} and must be at least 12 characters long and
 * include an uppercase letter, a lowercase letter, a digit, and a special character. Accepts
 * {@code CharSequence}.
 *
 * @author Myoung Ha, Ji
 */
@Documented
@Constraint(validatedBy = UserPasswordValidator.class)
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface Password {

  String message() default "Password must be at least 12 characters long and include an uppercase letter, a lowercase letter, a digit, and a special character.";

  Class<?>[] groups() default {};

  Class<? extends Payload>[] payload() default {};
}