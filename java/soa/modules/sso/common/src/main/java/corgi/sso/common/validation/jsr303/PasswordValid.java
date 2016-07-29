package corgi.sso.common.validation.jsr303;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created by huanghuanlai on 16/6/24.
 */
@Target({ElementType.METHOD, ElementType.FIELD, ElementType.ANNOTATION_TYPE, ElementType.CONSTRUCTOR, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = PasswordValidImpl.class)
public @interface PasswordValid {

    int PASS_MIN_SIZE = 8;
    int PASS_MAX_SIZE = 20;

    int min() default PASS_MIN_SIZE;
    int max() default PASS_MAX_SIZE;

    String message() default "passwd size between {min} the {max}";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
