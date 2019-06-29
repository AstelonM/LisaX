package com.lisaxdevelopment.lisax.utils;

import com.google.gson.JsonElement;
import com.google.gson.JsonIOException;
import com.google.gson.JsonParser;
import com.google.gson.JsonSyntaxException;
import com.google.gson.stream.JsonReader;

import java.io.Reader;
import java.io.StringReader;

public class JsonUtils {

    private static JsonParser parser = new JsonParser();

    public static JsonElement parse(String json) throws JsonSyntaxException {
        return parser.parse(new StringReader(json));
    }

    public static JsonElement parse(Reader json) throws JsonIOException, JsonSyntaxException {
        return parser.parse(json);
    }

    public static JsonElement parse(JsonReader json) throws JsonIOException, JsonSyntaxException {
        return parser.parse(json);
    }
}
