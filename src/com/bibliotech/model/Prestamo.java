package com.bibliotech.model;

import java.time.LocalDate;

public record Prestamo(int id, Libro libro, Socio socio, LocalDate fechaPrestamo, LocalDate fechaDevolucionEsperada) {
}
