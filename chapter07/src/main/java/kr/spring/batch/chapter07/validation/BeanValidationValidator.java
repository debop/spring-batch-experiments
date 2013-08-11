package kr.spring.batch.chapter07.validation;

import org.springframework.batch.item.validator.ValidationException;
import org.springframework.batch.item.validator.Validator;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.ValidatorFactory;
import java.util.Iterator;
import java.util.Set;

/**
 * kr.spring.batch.chapter07.validation.BeanValidationValidator
 *
 * @author 배성혁 sunghyouk.bae@gmail.com
 * @since 13. 8. 7. 오후 6:22
 */
public class BeanValidationValidator<T> implements Validator<T> {

	private ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
	private javax.validation.Validator validator = factory.getValidator();

	public void validate(T value) throws ValidationException {
		Set<ConstraintViolation<T>> violations = validator.validate(value);
		if (!violations.isEmpty()) {
			throw new ValidationException("Validation failed for " + value + ": " + violationsToString(violations));
		}
	}

	private String violationsToString(Set<ConstraintViolation<T>> violations) {
		String glue = ", ";
		StringBuilder builder = new StringBuilder();

		Iterator<ConstraintViolation<T>> iter = violations.iterator();
		while (iter.hasNext()) {
			ConstraintViolation<T> violation = iter.next();
			builder.append(violation.getPropertyPath())
			       .append(" ")
			       .append(violation.getMessage());

			if (iter.hasNext())
				builder.append(glue);
		}
		return builder.toString();
	}
}
