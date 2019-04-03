package beginfunc.annotations;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(value = RetentionPolicy.RUNTIME)
@Target({ ElementType.METHOD, ElementType.FIELD, ElementType.PARAMETER })
@Constraint(validatedBy = EnumValidatorImpl.class)
public @interface EnumValidator {

    Class<? extends Enum<?>> enumClass();

    String message() default "Value is not contained in the enum constants";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
