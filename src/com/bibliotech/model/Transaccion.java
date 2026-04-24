package com.bibliotech.model;

import java.time.LocalDate;

public record Transaccion(int id, TipoTransaccion tipo, Prestamo prestamo, LocalDate fecha) {
}