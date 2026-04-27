package com.bibliotech.repository;

import com.bibliotech.model.Transaccion;
import com.bibliotech.persistence.GsonConfig;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class TransaccionRepositoryJsonImpl implements TransaccionRepository {
    private static final String ARCHIVO = "data/transacciones.json";
    private final Gson gson = GsonConfig.get();
    private final List<Transaccion> transacciones;

    public TransaccionRepositoryJsonImpl() {
        this.transacciones = cargar();
    }

    private List<Transaccion> cargar() {
        File f = new File(ARCHIVO);
        if (!f.exists()) return new ArrayList<>();
        try (Reader r = new FileReader(f)) {
            List<Transaccion> lista = gson.fromJson(r, new TypeToken<List<Transaccion>>(){}.getType());
            return lista != null ? lista : new ArrayList<>();
        } catch (IOException e) {
            return new ArrayList<>();
        }
    }

    private void persistir() {
        new File("data").mkdirs();
        try (Writer w = new FileWriter(ARCHIVO)) {
            gson.toJson(transacciones, w);
        } catch (IOException e) {
            System.err.println("Error al persistir transacciones: " + e.getMessage());
        }
    }

    @Override
    public void guardar(Transaccion transaccion) {
        transacciones.add(transaccion);
        persistir();
    }

    @Override
    public List<Transaccion> buscarPorSocio(int socioId) {
        return transacciones.stream()
                .filter(t -> t.prestamo().socio().getId() == socioId)
                .toList();
    }
}