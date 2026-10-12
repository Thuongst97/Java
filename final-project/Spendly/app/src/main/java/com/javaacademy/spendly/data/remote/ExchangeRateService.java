package com.javaacademy.spendly.data.remote;

import java.io.IOException;

/** Looks up exchange rates. Blocking — call it from a background thread. */
public interface ExchangeRateService {

    /** How many units of {@code to} one unit of {@code from} buys. */
    double rate(String from, String to) throws IOException;
}
