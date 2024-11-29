package cloud_project.utils;

import cloud_project.entity.Role;
import cloud_project.entity.User;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class UserValidator implements ConstraintValidator<ValidUser, User> {
    @Override
    public boolean isValid(User user, ConstraintValidatorContext context) {

        boolean isValid = true;
        context.disableDefaultConstraintViolation();

        if (user.getRole() == Role.PROF) {
            if (user.getDepartment() == null || user.getDepartment().isBlank()) {
                context.buildConstraintViolationWithTemplate(
                                "Le département ne peut pas être vide pour un PROF")
                        .addPropertyNode("department")
                        .addConstraintViolation();
                isValid = false;
            }
            if (user.getSpecialization() == null || user.getSpecialization().isBlank()) {
                context.buildConstraintViolationWithTemplate(
                                "La spécialisation ne peut pas être vide pour un PROF")
                        .addPropertyNode("specialization")
                        .addConstraintViolation();
                isValid = false;
            }
        }


        if (user.getRole() == Role.ETUDIANT) {
            if (user.getMajor() == null || user.getMajor().isBlank()) {
                context.buildConstraintViolationWithTemplate(
                                "La filière ne peut pas être vide pour un ETUDIANT")
                        .addPropertyNode("major")
                        .addConstraintViolation();
                isValid = false;
            }

            if (user.getYearOfStudy() < 1) {
                context.buildConstraintViolationWithTemplate(
                                "L'année d'étude doit être supérieure à 0 pour un ETUDIANT")
                        .addPropertyNode("yearOfStudy")
                        .addConstraintViolation();
                isValid = false;
            }
        }

        return isValid;
    }
}