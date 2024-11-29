package cloud_project.utils;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.*;

@Target({ ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = ConditionalValidator.class)
public @interface ConditionalValidation {
    String message() default "Ce champ est requis";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
    String field();
    String value();
}