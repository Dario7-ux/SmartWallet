package org.example.gastos.application.ports.in;

import org.example.gastos.domain.models.Usuario;

import java.util.List;

public interface UsuarioIn {
    List<Usuario> listar();
    Usuario crear(Usuario usuario);
    Usuario buscarPorUsername(String username);
}

