package com.bibliotech.repository;

import com.bibliotech.model.Prestamo;
import com.bibliotech.persistence.GsonConfig;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.io.*;
import java.time.LocalDate;
import java.util.*;

public class PrestamoRepositoryJsonImpl implements PrestamoRepository {
    private static final String ARCHIVO = "data/prestamos.json";
    private final Gson gson = GsonConfig.get();
    private final Map<Long, Prestamo> prestamos;

    public PrestamoRepositoryJsonImpl() {
        this.prestamos = cargar();
    }

    private Map<Long, Prestamo> cargar() {
        File f = new File(ARCHIVO);
        if (!f.exists()) return new HashMap<>();
        try (Reader r = new FileReader(f)) {
            List<Prestamo> lista = gson.fromJson(r, new TypeToken<List<Prestamo>>(){}.getType());
            Map<Long, Prestamo> mapa = new HashMap<>();
            if (lista != null) lista.forEach(p -> mapa.put(p.id(), p));
            return mapa;
        } catch (IOException e) {
            return new HashMap<>();
        }
    }

    private void persistir() {
        new File("data").mkdirs();
        try (Writer w = new FileWriter(ARCHIVO)) {
            gson.toJson(new ArrayList<>(prestamos.values()), w);
        } catch (IOException e) {
            System.err.println("Error al persistir préstamos: " + e.getMessage());
        }
    }

    @Override
    public void guardar(Prestamo prestamo) {
        prestamos.put(prestamo.id(), prestamo);
        persistir();
    }

    @Override
    public void actualizar(Prestamo prestamo) {
        prestamos.put(prestamo.id(), prestamo);
        persistir();
    }

    @Override
    public Optional<Prestamo> buscarPorId(Long id) {
        return Optional.ofNullable(prestamos.get(id));
    }

    @Override
    public List<Prestamo> buscarTodos() {
        return new ArrayList<>(prestamos.values());
    }

    @Override
    public boolean estaDisponible(String isbn) {
        return prestamos.values().stream()
                .filter(p -> p.fechaDevolucionReal().isEmpty())
                .noneMatch(p -> p.recurso().isbn().equals(isbn));
    }

    @Override
    public long contarPrestamosSocio(String dni) {
        return prestamos.values().stream()
                .filter(p -> p.fechaDevolucionReal().isEmpty())
                .filter(p -> p.socio().getDni().equals(dni))
                .count();
    }
}