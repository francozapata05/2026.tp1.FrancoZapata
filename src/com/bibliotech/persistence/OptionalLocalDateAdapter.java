package com.bibliotech.persistence;

import com.google.gson.*;
import java.lang.reflect.Type;
import java.time.LocalDate;
import java.util.Optional;

public class OptionalLocalDateAdapter implements JsonSerializer<Optional<LocalDate>>, JsonDeserializer<Optional<LocalDate>> {
    @Override
    public JsonElement serialize(Optional<LocalDate> src, Type type, JsonSerializationContext ctx) {
        return src.map(d -> (JsonElement) new JsonPrimitive(d.toString())).orElse(JsonNull.INSTANCE);
    }

    @Override
    public Optional<LocalDate> deserialize(JsonElement json, Type type, JsonDeserializationContext ctx) {
        return json.isJsonNull() ? Optional.empty() : Optional.of(LocalDate.parse(json.getAsString()));
    }
}