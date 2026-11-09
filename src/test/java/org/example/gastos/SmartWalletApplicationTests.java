package org.example.gastos;

import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import static org.junit.jupiter.api.Assertions.assertTrue;

class SmartWalletApplicationTests {

    @Test
    void testBcrypt() {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        String validHash = "$2a$10$CpkoQnPZgLWaqjEyBPSB1uxiKeP8WJgt5c4VMHMxTGt0.9q23FTWq";
        assertTrue(encoder.matches("admin123", validHash), "El hash encriptado de admin123 debe ser valido");
    }
}
