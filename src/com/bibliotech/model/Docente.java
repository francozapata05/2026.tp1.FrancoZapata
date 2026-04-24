package com.bibliotech.model;

public class Docente extends Socio {
    public Docente(int id, String nombre, String apellido, String dni, String email) {
        super(id, nombre, apellido, dni, email);
    }

    @Override
    public int getLimitePrestamos() {
        return 5;
    }
}