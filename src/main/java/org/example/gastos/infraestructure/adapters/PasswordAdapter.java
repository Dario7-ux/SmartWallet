package org.example.gastos.infraestructure.adapters;

import org.example.gastos.application.ports.out.PasswordOut;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class PasswordAdapter implements PasswordOut {

    private final PasswordEncoder passwordEncoder;

    public PasswordAdapter(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public String encriptar(String texto) {
        return passwordEncoder.encode(texto);
    }
}

