package com.bibliotech.repository;

import com.bibliotech.model.Socio;
import com.bibliotech.persistence.GsonConfig;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.io.*;
import java.util.*;

public class SocioRepositoryJsonImpl implements SocioRepository {
    private static final String ARCHIVO = "data/socios.json";
    private final Gson gson = GsonConfig.get();
    private final Map<Integer, Socio> socios;

    public SocioRepositoryJsonImpl() {
        this.socios = cargar();
    }

    private Map<Integer, Socio> cargar() {
        File f = new File(ARCHIVO);
        if (!f.exists()) return new HashMap<>();
        try (Reader r = new FileReader(f)) {
            List<Socio> lista = gson.fromJson(r, new TypeToken<List<Socio>>(){}.getType());
            Map<Integer, Socio> mapa = new HashMap<>();
            if (lista != null) lista.forEach(s -> mapa.put(s.getId(), s));
            return mapa;
        } catch (IOException e) {
            return new HashMap<>();
        }
    }

    private void persistir() {
        new File("data").mkdirs();
        try (Writer w = new FileWriter(ARCHIVO)) {
            gson.toJson(new ArrayList<>(socios.values()), w);
        } catch (IOException e) {
            System.err.println("Error al persistir socios: " + e.getMessage());
        }
    }

    @Override
    public void guardar(Socio socio) {
        socios.put(socio.getId(), socio);
        persistir();
    }

    @Override
    public Optional<Socio> buscarPorId(Integer id) {
        return Optional.ofNullable(socios.get(id));
    }

    @Override
    public Optional<Socio> buscarPorDni(String dni) {
        return socios.values().stream()
                .filter(s -> s.getDni().equals(dni))
                .findFirst();
    }

    @Override
    public List<Socio> buscarTodos() {
        return new ArrayList<>(socios.values());
    }
}
