package com.bibliotech.persistence;

import com.bibliotech.model.Docente;
import com.bibliotech.model.Estudiante;
import com.bibliotech.model.Socio;
import com.google.gson.*;
import java.lang.reflect.Type;

public class SocioAdapter implements JsonSerializer<Socio>, JsonDeserializer<Socio> {
    @Override
    public JsonElement serialize(Socio src, Type type, JsonSerializationContext ctx) {
        JsonObject obj = new JsonObject();
        obj.addProperty("tipo", src instanceof Estudiante ? "ESTUDIANTE" : "DOCENTE");
        obj.addProperty("id", src.getId());
        obj.addProperty("nombre", src.getNombre());
        obj.addProperty("apellido", src.getApellido());
        obj.addProperty("dni", src.getDni());
        obj.addProperty("email", src.getEmail());
        return obj;
    }

    @Override
    public Socio deserialize(JsonElement json, Type type, JsonDeserializationContext ctx) {
        JsonObject obj = json.getAsJsonObject();
        int id = obj.get("id").getAsInt();
        String nombre = obj.get("nombre").getAsString();
        String apellido = obj.get("apellido").getAsString();
        String dni = obj.get("dni").getAsString();
        String email = obj.get("email").getAsString();
        return obj.get("tipo").getAsString().equals("ESTUDIANTE")
                ? new Estudiante(id, nombre, apellido, dni, email)
                : new Docente(id, nombre, apellido, dni, email);
    }
}