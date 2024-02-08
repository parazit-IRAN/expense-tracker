package ir.snapp.pay.annotation;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.List;

public class NoEmptyStringValidator implements ConstraintValidator<NoEmptyString, List<String>> {
	@Override
	public void initialize(NoEmptyString constraintAnnotation) {
	}

	@Override
	public boolean isValid(List<String> value, ConstraintValidatorContext context) {
		if (value == null || value.isEmpty()) {
			return false;
		}
		return value.stream().anyMatch(s -> !s.isEmpty());
	}
}
