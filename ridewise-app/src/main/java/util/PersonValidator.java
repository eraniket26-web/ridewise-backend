package util;

import java.util.regex.Pattern;

public class PersonValidator {

    private static final Pattern NAME_PATTERN = Pattern.compile("^[a-zA-Z\\s]{2,50}$");


    public void validateName(String name) {
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("Name cannot be null or empty.");
        }
        if (!NAME_PATTERN.matcher(name.trim()).matches()) {
            throw new IllegalArgumentException("Name must contain only letters and spaces (2-50 characters).");
        }
    }

    public void validateContactNo(long contactNo) {
        if (contactNo < 6000000000L || contactNo > 9999999999L) {
            throw new IllegalArgumentException("Contact number must be a valid 10-digit mobile number.");
        }
    }
}
