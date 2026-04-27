package com.bibliotech.model;

import java.time.LocalDate;

public record Sancion(int id, Socio socio, LocalDate fechaInicio, LocalDate fechaFin) {
}