package com.bibliotech.model;

public abstract class Socio {
    private final int id;
    private final String nombre;
    private final String apellido;
    private final String dni;
    private final String email;

    public Socio(int id, String nombre, String apellido, String dni, String email) {
        this.id = id;
        this.nombre = nombre;
        this.apellido = apellido;
        this.dni = dni;
        this.email = email;
    }

    public int getId() { return id; }
    public String getNombre() { return nombre; }
    public String getApellido() { return apellido; }
    public String getDni() { return dni; }
    public String getEmail() { return email; }

    public abstract int getLimitePrestamos();
}
