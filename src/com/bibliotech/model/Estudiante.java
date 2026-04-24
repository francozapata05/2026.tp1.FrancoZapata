package com.bibliotech.model;

public class Estudiante extends Socio {
    public Estudiante(int id, String nombre, String apellido, String dni, String email) {
        super(id, nombre, apellido, dni, email);
    }

    @Override
    public int getLimitePrestamos() {
        return 3;
    }
}