package org.example.gastos.infraestructure.adapters;

import org.example.gastos.application.ports.out.UsuarioOut;
import org.example.gastos.domain.exception.RecursoNoEncontradoException;
import org.example.gastos.domain.models.Usuario;
import org.example.gastos.infraestructure.entities.UsuarioEntity;
import org.example.gastos.infraestructure.mappers.UsuarioMapper;
import org.example.gastos.infraestructure.repository.UsuarioJpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class UsuarioAdapter implements UsuarioOut {

    private final UsuarioJpaRepository usuarioRepository;

    public UsuarioAdapter(UsuarioJpaRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    @Override
    public List<Usuario> listar() {
        return usuarioRepository.findAll().stream()
                .map(UsuarioMapper::toDomain)
                .toList();
    }

    @Override
    public Optional<Usuario> buscarPorUsername(String username) {
        return usuarioRepository.findByUsername(username)
                .map(UsuarioMapper::toDomain);
    }

    @Override
    public Usuario guardar(Usuario usuario) {
        UsuarioEntity entity = (usuario.getId() == null) ? new UsuarioEntity() :
                usuarioRepository.findById(usuario.getId())
                        .orElseThrow(() -> new RecursoNoEncontradoException("Usuario no existe"));

        UsuarioMapper.toEntity(usuario, entity);
        return UsuarioMapper.toDomain(usuarioRepository.save(entity));
    }

    @Override
    public boolean existePorUsername(String username) {
        return usuarioRepository.existsByUsername(username);
    }

    @Override
    public boolean existeAlguno() {
        return usuarioRepository.count() > 0;
    }
}

