package com.javaacademy.spendly.data.remote;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;

/**
 * Fetches rates from the free, key-less Frankfurter API (European Central Bank data) using plain
 * {@link HttpURLConnection} — no extra library needed (lesson 21 also shows Retrofit).
 */
public final class FrankfurterRateService implements ExchangeRateService {

    private static final String BASE_URL = "https://api.frankfurter.dev/v1/latest";

    @Override
    public double rate(String from, String to) throws IOException {
        if (from.equals(to)) return 1.0;
        URL url = new URL(BASE_URL + "?base=" + from + "&symbols=" + to);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setConnectTimeout(10_000);
        connection.setReadTimeout(10_000);
        connection.setRequestProperty("Accept", "application/json");
        try {
            int code = connection.getResponseCode();
            if (code != HttpURLConnection.HTTP_OK) throw new IOException("HTTP " + code);
            try (InputStream in = connection.getInputStream()) {
                return RateResponseParser.parseRate(readUtf8(in), to);
            }
        } finally {
            connection.disconnect();
        }
    }

    private static String readUtf8(InputStream in) throws IOException {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        byte[] buffer = new byte[8192];
        int n;
        while ((n = in.read(buffer)) != -1) out.write(buffer, 0, n);
        return out.toString(StandardCharsets.UTF_8.name());
    }
}
