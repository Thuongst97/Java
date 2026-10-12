package com.javaacademy.spendly.data.remote;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonParser;

import java.io.IOException;

/**
 * Parses a Frankfurter API response such as
 * <pre>{"amount":1.0,"base":"USD","date":"2026-03-13","rates":{"EUR":0.92}}</pre>
 * with Gson's tree model (no reflection, so R8 needs no keep rules).
 */
public final class RateResponseParser {

    private RateResponseParser() { }

    public static double parseRate(String json, String to) throws IOException {
        try {
            JsonObject root = JsonParser.parseString(json).getAsJsonObject();
            JsonObject rates = root.getAsJsonObject("rates");
            JsonElement rate = rates == null ? null : rates.get(to);
            if (rate == null || rate.isJsonNull()) throw new IOException("No rate for " + to);
            double value = rate.getAsDouble();
            if (!(value > 0)) throw new IOException("Invalid rate " + value + " for " + to);
            return value;
        } catch (JsonParseException | IllegalStateException | ClassCastException | NumberFormatException e) {
            throw new IOException("Malformed rate response", e);
        }
    }
}
