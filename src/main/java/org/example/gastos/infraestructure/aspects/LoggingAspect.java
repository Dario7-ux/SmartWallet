package org.example.gastos.infraestructure.aspects;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class LoggingAspect {

    private static final Logger log = LoggerFactory.getLogger(LoggingAspect.class);

    private int totalOperaciones = 0;
    private int totalGastosRegistrados = 0;
    private int totalPresupuestosEvaluados = 0;

    @Around("execution(* org.example.gastos.application.services..*(..))")
    public Object auditarYMedirTiempo(ProceedingJoinPoint joinPoint) throws Throwable {
        long inicio = System.currentTimeMillis();
        String clase = joinPoint.getTarget().getClass().getSimpleName();
        String metodo = joinPoint.getSignature().getName();

        totalOperaciones++;
        if (metodo.startsWith("registrar")) {
            totalGastosRegistrados++;
        } else if (metodo.startsWith("evaluar")) {
            totalPresupuestosEvaluados++;
        }

        log.info("[AUDITORIA SMARTWALLET] -> Ejecutando: {}.{}() | Total Operaciones: {} | Gastos Registrados: {} | Presupuestos Evaluados: {}",
                clase, metodo, totalOperaciones, totalGastosRegistrados, totalPresupuestosEvaluados);

        try {
            Object resultado = joinPoint.proceed();
            long duracion = System.currentTimeMillis() - inicio;
            log.info("[TIEMPO RESPUESTA] -> {}.{}() completado exitosamente en {} ms",
                    clase, metodo, duracion);
            return resultado;
        } catch (Throwable ex) {
            long duracion = System.currentTimeMillis() - inicio;
            log.error("[ERROR AUDITORIA] -> {}.{}() falló tras {} ms con excepción: {} - {}",
                    clase, metodo, duracion, ex.getClass().getSimpleName(), ex.getMessage());
            throw ex;
        }
    }
}

