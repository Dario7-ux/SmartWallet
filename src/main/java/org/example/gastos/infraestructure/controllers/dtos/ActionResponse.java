package org.example.gastos.infraestructure.controllers.dtos;

public record ActionResponse<T>(String message, T results) {
}

