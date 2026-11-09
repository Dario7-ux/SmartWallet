package org.example.gastos.application.services;

import org.example.gastos.application.ports.in.UsuarioIn;
import org.example.gastos.application.ports.out.PasswordOut;
import org.example.gastos.application.ports.out.UsuarioOut;
import org.example.gastos.domain.exception.RecursoNoEncontradoException;
import org.example.gastos.domain.exception.ReglaNegocioException;
import org.example.gastos.domain.models.Usuario;

import java.util.List;

public class UsuarioService implements UsuarioIn {

    private final UsuarioOut usuarioOut;
    private final PasswordOut passwordOut;

    public UsuarioService(UsuarioOut usuarioOut, PasswordOut passwordOut) {
        this.usuarioOut = usuarioOut;
        this.passwordOut = passwordOut;
    }

    @Override
    public List<Usuario> listar() {
        return usuarioOut.listar();
    }

    @Override
    public Usuario crear(Usuario usuario) {
        if (usuarioOut.existePorUsername(usuario.getUsername())) {
            throw new ReglaNegocioException("El usuario con username '" + usuario.getUsername() + "' ya existe");
        }

        Usuario nuevo = Usuario.nuevo(
                usuario.getUsername(),
                passwordOut.encriptar(usuario.getPassword()),
                usuario.getRol()
        );

        return usuarioOut.guardar(nuevo);
    }

    @Override
    public Usuario buscarPorUsername(String username) {
        return usuarioOut.buscarPorUsername(username)
                .orElseThrow(() -> new RecursoNoEncontradoException("Usuario '" + username + "' no encontrado"));
    }
}

