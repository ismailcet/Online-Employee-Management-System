package com.ismailcet.employeemanagement.utils;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class PasswordUtils {

    private static final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    public static String hashPassword(String password){
        return passwordEncoder.encode(password);
    }
    public static boolean verifyPassword(String hashPassword, String encodePassword){
        return passwordEncoder.matches(hashPassword, encodePassword);
    }
}
