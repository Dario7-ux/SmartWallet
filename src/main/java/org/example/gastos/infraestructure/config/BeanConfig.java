package org.example.gastos.infraestructure.config;

import org.example.gastos.application.ports.out.*;
import org.example.gastos.application.services.GastoService;
import org.example.gastos.application.services.PresupuestoService;
import org.example.gastos.application.services.UsuarioService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class BeanConfig {

    @Bean
    public GastoService gastoService(
            GastoRepositoryPort gastoRepositoryPort,
            PresupuestoRepositoryPort presupuestoRepositoryPort,
            CategoriaRepositoryPort categoriaRepositoryPort,
            NotificacionPort notificacionPort,
            TipoCambioPort tipoCambioPort) {
        return new GastoService(
                gastoRepositoryPort,
                presupuestoRepositoryPort,
                categoriaRepositoryPort,
                notificacionPort,
                tipoCambioPort
        );
    }

    @Bean
    public PresupuestoService presupuestoService(
            PresupuestoRepositoryPort presupuestoRepositoryPort,
            CategoriaRepositoryPort categoriaRepositoryPort,
            NotificacionPort notificacionPort) {
        return new PresupuestoService(
                presupuestoRepositoryPort,
                categoriaRepositoryPort,
                notificacionPort
        );
    }

    @Bean
    public UsuarioService usuarioService(UsuarioOut usuarioOut, PasswordOut passwordOut) {
        return new UsuarioService(usuarioOut, passwordOut);
    }
}
