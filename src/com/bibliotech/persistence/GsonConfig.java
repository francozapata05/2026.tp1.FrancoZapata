package com.bibliotech.persistence;

import com.bibliotech.model.Recurso;
import com.bibliotech.model.Socio;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import java.time.LocalDate;
import java.util.Optional;

public class GsonConfig {
    private static final Gson INSTANCE = new GsonBuilder()
            .registerTypeAdapter(LocalDate.class, new LocalDateAdapter())
            .registerTypeAdapter(new TypeToken<Optional<LocalDate>>(){}.getType(), new OptionalLocalDateAdapter())
            .registerTypeHierarchyAdapter(Socio.class, new SocioAdapter())
            .registerTypeHierarchyAdapter(Recurso.class, new RecursoAdapter())
            .setPrettyPrinting()
            .create();

    public static Gson get() {
        return INSTANCE;
    }
}