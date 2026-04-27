package com.bibliotech.persistence;

import com.bibliotech.model.*;
import com.google.gson.*;
import java.lang.reflect.Type;

public class RecursoAdapter implements JsonSerializer<Recurso>, JsonDeserializer<Recurso> {
    @Override
    public JsonElement serialize(Recurso src, Type type, JsonSerializationContext ctx) {
        JsonObject obj = new JsonObject();
        if (src instanceof Libro l) {
            obj.addProperty("tipo", "LIBRO");
            obj.addProperty("isbn", l.isbn());
            obj.addProperty("titulo", l.titulo());
            obj.addProperty("autor", l.autor());
            obj.addProperty("anio", l.anio());
            obj.addProperty("categoria", l.categoria().name());
        } else if (src instanceof Ebook e) {
            obj.addProperty("tipo", "EBOOK");
            obj.addProperty("isbn", e.isbn());
            obj.addProperty("titulo", e.titulo());
            obj.addProperty("autor", e.autor());
            obj.addProperty("paginas", e.paginas());
            obj.addProperty("categoria", e.categoria().name());
            obj.addProperty("formato", e.formato().name());
        }
        return obj;
    }

    @Override
    public Recurso deserialize(JsonElement json, Type type, JsonDeserializationContext ctx) {
        JsonObject obj = json.getAsJsonObject();
        String isbn = obj.get("isbn").getAsString();
        String titulo = obj.get("titulo").getAsString();
        String autor = obj.get("autor").getAsString();
        Categoria categoria = Categoria.valueOf(obj.get("categoria").getAsString());

        if (obj.get("tipo").getAsString().equals("LIBRO")) {
            int anio = obj.get("anio").getAsInt();
            return new Libro(isbn, titulo, autor, anio, categoria);
        } else {
            int paginas = obj.get("paginas").getAsInt();
            FormatoEbook formato = FormatoEbook.valueOf(obj.get("formato").getAsString());
            return new Ebook(isbn, titulo, autor, paginas, categoria, formato);
        }
    }
}
