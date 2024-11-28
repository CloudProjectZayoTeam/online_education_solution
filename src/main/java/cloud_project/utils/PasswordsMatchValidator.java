package cloud_project.utils;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.lang.reflect.Field;

public class PasswordsMatchValidator implements ConstraintValidator<PasswordsMatch, Object> {

    @Override
    public boolean isValid(Object dto, ConstraintValidatorContext context) {
        if (dto == null) {
            return true;
        }

        try {
            Field passwordField;
            Field confirmPasswordField;

            try {
                // Essayer de trouver les champs "password" et "confirmPassword"
                passwordField = dto.getClass().getDeclaredField("password");
                confirmPasswordField = dto.getClass().getDeclaredField("confirmPassword");

            } catch (NoSuchFieldException e) {
                // Si les champs précédents ne sont pas trouvés, chercher "newPassword" et "confirmNewPassword"
                passwordField = dto.getClass().getDeclaredField("newPassword");
                confirmPasswordField = dto.getClass().getDeclaredField("confirmNewPassword");
            }

            // Rendre les champs accessibles
            passwordField.setAccessible(true);
            confirmPasswordField.setAccessible(true);

            // Obtenir les valeurs des champs
            String password = (String) passwordField.get(dto);
            String confirmPassword = (String) confirmPasswordField.get(dto);

            // Vérifier si les mots de passe correspondent
            if (password != null && !password.equals(confirmPassword)) {
                // Désactiver la violation par défaut et ajouter un message personnalisé
                context.disableDefaultConstraintViolation();
                context.buildConstraintViolationWithTemplate("Les mots de passe ne correspondent pas.")
                        .addPropertyNode(confirmPasswordField.getName())
                        .addConstraintViolation();
                return false;
            }
        } catch (NoSuchFieldException | IllegalAccessException e) {
            throw new RuntimeException("Erreur d'accès aux champs de mot de passe", e);
        }

        return true;
    }
}
