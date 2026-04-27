package com.bibliotech.exception;

import java.time.LocalDate;

public class SocioSancionadoException extends BibliotecaException {
    public SocioSancionadoException(String dni, LocalDate fechaFin) {
        super("El socio con DNI " + dni + " está sancionado hasta el " + fechaFin + ".");
    }
}