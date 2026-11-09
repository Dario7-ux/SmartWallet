package org.example.gastos.infraestructure.securite;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
public class LogInInterceptor implements HandlerInterceptor {

    private static final Logger log = LoggerFactory.getLogger(LogInInterceptor.class);
    private static final String INICIO = "Inicializacion";

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        request.setAttribute(INICIO, System.currentTimeMillis());
        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        Object inicioAttr = request.getAttribute(INICIO);
        if (inicioAttr == null) {
            return;
        }
        long inicio = (Long) inicioAttr;
        long duracion = System.currentTimeMillis() - inicio;

        log.info("-- {} {} - {} - {} ms",
                request.getMethod(), request.getRequestURI(), usuarioActual(), duracion);
    }

    private String usuarioActual() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || auth instanceof AnonymousAuthenticationToken) {
            return "Anonimo";
        }
        return auth.getName();
    }
}

