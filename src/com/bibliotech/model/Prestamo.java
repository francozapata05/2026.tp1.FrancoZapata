package com.bibliotech.model;

import java.time.LocalDate;
import java.util.Optional;

public record Prestamo(long id, Libro libro, Socio socio, LocalDate fechaPrestamo, LocalDate fechaDevolucionEsperada, Optional<LocalDate> fechaDevolucionReal) {
}