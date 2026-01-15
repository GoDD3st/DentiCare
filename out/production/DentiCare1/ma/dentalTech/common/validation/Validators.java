package ma.dentalTech.common.validation;

import ma.dentalTech.common.exceptions.ValidationException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.regex.Pattern;

// Marouane
public class Validators {
    
    // Patterns de validation
    private static final Pattern EMAIL_PATTERN = Pattern.compile(
        "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$"
    );
    
    private static final Pattern PHONE_PATTERN = Pattern.compile(
        "^[0-9]{10}$|^\\+212[0-9]{9}$|^0[0-9]{9}$"
    );
    
    private static final Pattern CIN_PATTERN = Pattern.compile(
        "^[A-Z]{1,2}[0-9]{5,6}$"
    );
    
    private static final Pattern LOGIN_PATTERN = Pattern.compile(
        "^[a-zA-Z0-9_]{3,50}$"
    );
    
    // Empêcher l'instanciation
    private Validators() {
        throw new AssertionError("Cette classe ne doit pas être instanciée");
    }

    public static void validateNotEmpty(String value, String fieldName) {
        if (value == null || value.trim().isEmpty()) {
            throw new ValidationException(fieldName + " ne peut pas être vide");
        }
    }

    public static void validateNotNull(Object value, String fieldName) {
        if (value == null) {
            throw new ValidationException(fieldName + " ne peut pas être null");
        }
    }

    public static void validateLength(String value, int minLength, int maxLength, String fieldName) {
        if (value == null) {
            throw new ValidationException(fieldName + " ne peut pas être null");
        }
        if (value.length() < minLength) {
            throw new ValidationException(fieldName + " doit contenir au moins " + minLength + " caractères");
        }
        if (value.length() > maxLength) {
            throw new ValidationException(fieldName + " ne peut pas dépasser " + maxLength + " caractères");
        }
    }

    public static void validateEmail(String email) {
        if (email == null || email.trim().isEmpty()) {
            throw new ValidationException("L'email ne peut pas être vide");
        }
        if (!EMAIL_PATTERN.matcher(email).matches()) {
            throw new ValidationException("Format d'email invalide");
        }
    }

    public static void validatePhone(String phone) {
        if (phone == null || phone.trim().isEmpty()) {
            throw new ValidationException("Le numéro de téléphone ne peut pas être vide");
        }
        String cleanedPhone = phone.replaceAll("[\\s-]", "");
        if (!PHONE_PATTERN.matcher(cleanedPhone).matches()) {
            throw new ValidationException("Format de téléphone invalide. Format attendu: 0612345678 ou +212612345678");
        }
    }

    public static void validateCIN(String cin) {
        if (cin == null || cin.trim().isEmpty()) {
            throw new ValidationException("Le CIN ne peut pas être vide");
        }
        if (!CIN_PATTERN.matcher(cin.toUpperCase()).matches()) {
            throw new ValidationException("Format de CIN invalide. Format attendu: AB12345");
        }
    }

    public static void validateLogin(String login) {
        if (login == null || login.trim().isEmpty()) {
            throw new ValidationException("Le login ne peut pas être vide");
        }
        if (!LOGIN_PATTERN.matcher(login).matches()) {
            throw new ValidationException("Format de login invalide. Le login doit contenir entre 3 et 50 caractères (lettres, chiffres et underscore uniquement)");
        }
    }

    public static void validatePassword(String password) {
        if (password == null || password.isEmpty()) {
            throw new ValidationException("Le mot de passe ne peut pas être vide");
        }
        if (password.length() < 6) {
            throw new ValidationException("Le mot de passe doit contenir au moins 6 caractères");
        }
        if (password.length() > 255) {
            throw new ValidationException("Le mot de passe ne peut pas dépasser 255 caractères");
        }
    }

    public static void validateDateNotFuture(LocalDate date, String fieldName) {
        if (date == null) {
            throw new ValidationException(fieldName + " ne peut pas être null");
        }
        if (date.isAfter(LocalDate.now())) {
            throw new ValidationException(fieldName + " ne peut pas être dans le futur");
        }
    }

    public static void validateDateNotPast(LocalDate date, String fieldName) {
        if (date == null) {
            throw new ValidationException(fieldName + " ne peut pas être null");
        }
        if (date.isBefore(LocalDate.now())) {
            throw new ValidationException(fieldName + " ne peut pas être dans le passé");
        }
    }

    public static void validateDateRange(LocalDate date, LocalDate minDate, LocalDate maxDate, String fieldName) {
        if (date == null) {
            throw new ValidationException(fieldName + " ne peut pas être null");
        }
        if (minDate != null && date.isBefore(minDate)) {
            throw new ValidationException(fieldName + " ne peut pas être avant " + minDate);
        }
        if (maxDate != null && date.isAfter(maxDate)) {
            throw new ValidationException(fieldName + " ne peut pas être après " + maxDate);
        }
    }

    public static void validatePositive(Number value, String fieldName) {
        if (value == null) {
            throw new ValidationException(fieldName + " ne peut pas être null");
        }
        if (value.doubleValue() < 0) {
            throw new ValidationException(fieldName + " doit être positif");
        }
    }

    public static void validateStrictlyPositive(Number value, String fieldName) {
        if (value == null) {
            throw new ValidationException(fieldName + " ne peut pas être null");
        }
        if (value.doubleValue() <= 0) {
            throw new ValidationException(fieldName + " doit être strictement positif");
        }
    }

    public static void validateRange(Number value, Number min, Number max, String fieldName) {
        if (value == null) {
            throw new ValidationException(fieldName + " ne peut pas être null");
        }
        double val = value.doubleValue();
        if (min != null && val < min.doubleValue()) {
            throw new ValidationException(fieldName + " doit être supérieur ou égal à " + min);
        }
        if (max != null && val > max.doubleValue()) {
            throw new ValidationException(fieldName + " doit être inférieur ou égal à " + max);
        }
    }

    public static void validateBirthDate(LocalDate birthDate) {
        if (birthDate == null) {
            throw new ValidationException("La date de naissance ne peut pas être null");
        }
        LocalDate now = LocalDate.now();
        if (birthDate.isAfter(now)) {
            throw new ValidationException("La date de naissance ne peut pas être dans le futur");
        }
        // Valider qu'on n'a pas plus de 150 ans
        LocalDate minDate = now.minusYears(150);
        if (birthDate.isBefore(minDate)) {
            throw new ValidationException("La date de naissance n'est pas valide");
        }
    }

    public static void validateDateOrder(LocalDate startDate, LocalDate endDate, String startFieldName, String endFieldName) {
        if (startDate == null || endDate == null) {
            throw new ValidationException(startFieldName + " et " + endFieldName + " ne peuvent pas être null");
        }
        if (startDate.isAfter(endDate)) {
            throw new ValidationException(startFieldName + " doit être avant " + endFieldName);
        }
    }

    public static void validateDateTimeOrder(LocalDateTime startDateTime, LocalDateTime endDateTime, 
                                            String startFieldName, String endFieldName) {
        if (startDateTime == null || endDateTime == null) {
            throw new ValidationException(startFieldName + " et " + endFieldName + " ne peuvent pas être null");
        }
        if (startDateTime.isAfter(endDateTime)) {
            throw new ValidationException(startFieldName + " doit être avant " + endFieldName);
        }
    }
}

