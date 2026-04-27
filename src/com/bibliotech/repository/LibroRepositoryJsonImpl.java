package com.bibliotech.repository;

import com.bibliotech.model.Recurso;
import com.bibliotech.persistence.GsonConfig;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.io.*;
import java.util.*;

public class LibroRepositoryJsonImpl implements LibroRepository {
    private static final String ARCHIVO = "data/recursos.json";
    private final Gson gson = GsonConfig.get();
    private final Map<String, Recurso> recursos;

    public LibroRepositoryJsonImpl() {
        this.recursos = cargar();
    }

    private Map<String, Recurso> cargar() {
        File f = new File(ARCHIVO);
        if (!f.exists()) return new HashMap<>();
        try (Reader r = new FileReader(f)) {
            List<Recurso> lista = gson.fromJson(r, new TypeToken<List<Recurso>>(){}.getType());
            Map<String, Recurso> mapa = new HashMap<>();
            if (lista != null) lista.forEach(rec -> mapa.put(rec.isbn(), rec));
            return mapa;
        } catch (IOException e) {
            return new HashMap<>();
        }
    }

    private void persistir() {
        new File("data").mkdirs();
        try (Writer w = new FileWriter(ARCHIVO)) {
            gson.toJson(new ArrayList<>(recursos.values()), w);
        } catch (IOException e) {
            System.err.println("Error al persistir recursos: " + e.getMessage());
        }
    }

    @Override
    public void guardar(Recurso recurso) {
        recursos.put(recurso.isbn(), recurso);
        persistir();
    }

    @Override
    public Optional<Recurso> buscarPorId(String isbn) {
        return Optional.ofNullable(recursos.get(isbn));
    }

    @Override
    public List<Recurso> buscarTodos() {
        return new ArrayList<>(recursos.values());
    }
}