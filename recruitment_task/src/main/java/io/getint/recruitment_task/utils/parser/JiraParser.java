package io.getint.recruitment_task.utils.parser;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.util.List;
import java.util.stream.StreamSupport;

public final class JiraParser {
  private static final Gson gson = new Gson();

  private JiraParser() {
    throw new UnsupportedOperationException(
        "JiraParser can't be instanced - use static methods instead");
  }

  public static <T> List<T> parseToList(String json, Class<T> clazz) {
    // 1. Get class name without DTO suffix for parser use
    // Not really happy with hardcoding 's' at the end of the key, but I prefer it that way than to
    // rename all the DTOs to be plural
    String key =
        clazz.getSimpleName().toLowerCase().substring(0, clazz.getSimpleName().length() - 3) + "s";
    // 2. Parse String as a JSON object
    JsonObject root = JsonParser.parseString(json).getAsJsonObject();
    // 3. Get specific JSON array from the object
    JsonArray object = root.getAsJsonArray(key);

    // 4. Map the JSON into proper DTO
    return StreamSupport.stream(object.spliterator(), false)
        .map(x -> gson.fromJson(x, clazz))
        .toList();
  }

  public static <T> String parseToString(List<T> object) {
    return gson.toJson(object);
  }

  public static <T> String parseToString(T object) {
    return gson.toJson(object);
  }
}
