package org.example.gastos.infraestructure.controllers;

import org.example.gastos.domain.exception.CategoriaNoEncontradaException;
import org.example.gastos.domain.exception.DatoInvalidoException;
import org.example.gastos.domain.exception.PresupuestoExcedidoException;
import org.example.gastos.infraestructure.controllers.dtos.ActionResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(PresupuestoExcedidoException.class)
    public ResponseEntity<ActionResponse<String>> handlePresupuestoExcedido(PresupuestoExcedidoException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new ActionResponse<>("Operación rechazada: " + ex.getMessage(), null));
    }

    @ExceptionHandler(CategoriaNoEncontradaException.class)
    public ResponseEntity<ActionResponse<String>> handleCategoriaNoEncontrada(CategoriaNoEncontradaException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(new ActionResponse<>(ex.getMessage(), null));
    }

    @ExceptionHandler(DatoInvalidoException.class)
    public ResponseEntity<ActionResponse<String>> handleDatoInvalido(DatoInvalidoException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new ActionResponse<>(ex.getMessage(), null));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ActionResponse<Map<String, String>>> handleValidationExceptions(MethodArgumentNotValidException ex) {
        Map<String, String> errores = new HashMap<>();
        for (FieldError error : ex.getBindingResult().getFieldErrors()) {
            errores.put(error.getField(), error.getDefaultMessage());
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new ActionResponse<>("Error de validación en los datos enviados", errores));
    }

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<ActionResponse<String>> handleBadCredentials(BadCredentialsException ex) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(new ActionResponse<>("Credenciales incorrectas", null));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ActionResponse<String>> handleGeneral(Exception ex) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new ActionResponse<>("Error interno del servidor: " + ex.getMessage(), null));
    }
}

