package com.bibliotech.repository;

import com.bibliotech.model.Sancion;
import com.bibliotech.persistence.GsonConfig;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.io.*;
import java.time.LocalDate;
import java.util.*;

public class SancionRepositoryJsonImpl implements SancionRepository {
    private static final String ARCHIVO = "data/sanciones.json";
    private final Gson gson = GsonConfig.get();
    private final List<Sancion> sanciones;

    public SancionRepositoryJsonImpl() {
        this.sanciones = cargar();
    }

    private List<Sancion> cargar() {
        File f = new File(ARCHIVO);
        if (!f.exists()) return new ArrayList<>();
        try (Reader r = new FileReader(f)) {
            List<Sancion> lista = gson.fromJson(r, new TypeToken<List<Sancion>>(){}.getType());
            return lista != null ? lista : new ArrayList<>();
        } catch (IOException e) {
            return new ArrayList<>();
        }
    }

    private void persistir() {
        new File("data").mkdirs();
        try (Writer w = new FileWriter(ARCHIVO)) {
            gson.toJson(sanciones, w);
        } catch (IOException e) {
            System.err.println("Error al persistir sanciones: " + e.getMessage());
        }
    }

    @Override
    public void guardar(Sancion sancion) {
        sanciones.add(sancion);
        persistir();
    }

    @Override
    public List<Sancion> buscarPorSocio(int socioId) {
        return sanciones.stream()
                .filter(s -> s.socio().getId() == socioId)
                .toList();
    }

    @Override
    public Optional<Sancion> buscarSancionActiva(int socioId) {
        return sanciones.stream()
                .filter(s -> s.socio().getId() == socioId)
                .filter(s -> !s.fechaFin().isBefore(LocalDate.now()))
                .max(Comparator.comparing(Sancion::fechaFin));
    }
}