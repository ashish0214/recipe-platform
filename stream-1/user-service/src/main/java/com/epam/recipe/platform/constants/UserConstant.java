package com.epam.recipe.platform.constants;

public interface UserConstant {
    String FIRST_NAME_NOT_BLANK_MESSAGE = "First name cannot be blank";
    String LAST_NAME_NON_BLANK_MESSAGE = "Last name cannot be blank";
    String EMAIL_NON_NULL_MESSAGE = "Email cannot be blank";
    String EMAIL_VALID_MESSAGE = "Invalid email format";
    String EMAIL_ALREADY_EXISTS_MESSAGE = "Email already exists";
    String PASSWORD_VALID_MESSAGE = "Password must be at least 8 characters long, contain at least one digit, one uppercase letter, one lowercase letter, and one special character, and must not contain whitespace";
    String PASSWORD_VALIDATION_EXPRESSION = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[!@#$%^&*()-_=+{};:',<.>/?])(?=\\S+$).{8,}$";
    String USER_REGISTRATION_SUCCESS_MESSAGE = "User registered successfully";
    String INVALID_DATA_MESSAGE = "Invalid input data";
    String ROLE_USER = "USER";
    String MAIL_CONTENT = "Welcome to Recipe Platform";
    String OTP_NON_NULL_MESSAGE= "OTP cannot be null";
    String PASSWORD_NON_NULL_MESSAGE = "Password cannot be blank";
    String OTP_DIGITS_MESSAGE = "OTP must be a 6-digit number";
    String OTP_SIZE_MESSAGE = "OTP must be exactly 6 digits";
    String PASSWORD_CHANGED_MESSAGE = "Password Changed Successfully";
}
