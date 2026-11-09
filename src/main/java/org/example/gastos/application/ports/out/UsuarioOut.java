package org.example.gastos.application.ports.out;

import org.example.gastos.domain.models.Usuario;

import java.util.List;
import java.util.Optional;

public interface UsuarioOut {
    List<Usuario> listar();
    Optional<Usuario> buscarPorUsername(String username);
    Usuario guardar(Usuario usuario);
    boolean existePorUsername(String username);
    boolean existeAlguno();
}

