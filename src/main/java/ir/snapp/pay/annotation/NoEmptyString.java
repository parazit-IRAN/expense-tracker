package ir.snapp.pay.annotation;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = NoEmptyStringValidator.class)
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface NoEmptyString {
	String message() default "List cannot contain just an empty string";

	Class<?>[] groups() default {};

	Class<? extends Payload>[] payload() default {};
}
