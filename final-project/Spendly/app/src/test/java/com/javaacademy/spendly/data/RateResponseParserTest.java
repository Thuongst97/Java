package com.javaacademy.spendly.data;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

import com.javaacademy.spendly.data.remote.RateResponseParser;

import org.junit.Test;

import java.io.IOException;

public class RateResponseParserTest {

    @Test
    public void readsTheRequestedRate() throws IOException {
        String json = "{\"amount\":1.0,\"base\":\"USD\",\"date\":\"2026-03-13\",\"rates\":{\"EUR\":0.9213}}";
        assertEquals(0.9213, RateResponseParser.parseRate(json, "EUR"), 1e-9);
    }

    @Test
    public void missingOrMalformedDataIsAnIOException() {
        assertThrows(IOException.class, () -> RateResponseParser.parseRate("{\"rates\":{}}", "EUR"));
        assertThrows(IOException.class, () -> RateResponseParser.parseRate("{\"message\":\"not found\"}", "EUR"));
        assertThrows(IOException.class, () -> RateResponseParser.parseRate("<html>oops</html>", "EUR"));
        assertThrows(IOException.class, () -> RateResponseParser.parseRate("{\"rates\":{\"EUR\":\"abc\"}}", "EUR"));
        assertThrows(IOException.class, () -> RateResponseParser.parseRate("{\"rates\":{\"EUR\":-1}}", "EUR"));
    }
}
