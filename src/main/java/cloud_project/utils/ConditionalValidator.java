package cloud_project.utils;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.beans.BeanWrapperImpl;

public class ConditionalValidator implements ConstraintValidator<ConditionalValidation, Object> {
    private String field;
    private String value;

    @Override
    public void initialize(ConditionalValidation constraintAnnotation) {
        this.field = constraintAnnotation.field();
        this.value = constraintAnnotation.value();
    }

    @Override
    public boolean isValid(Object value, ConstraintValidatorContext context) {
        if (value == null) {
            return true;
        }

        try {
            BeanWrapperImpl beanWrapper = new BeanWrapperImpl(value);
            Object fieldValue = beanWrapper.getPropertyValue(field);

            // Si le champ conditionnel (role) correspond à la valeur attendue
            if (fieldValue != null && fieldValue.toString().equals(this.value)) {
                // Le champ validé ne doit pas être null ou vide
                return value != null && !value.toString().trim().isEmpty();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Si le champ conditionnel ne correspond pas, pas besoin de validation
        return true;
    }
}