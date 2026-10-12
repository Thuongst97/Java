package com.javaacademy.spendly.ui.list;

/** The state of the "convert this month's total" feature. */
public record Conversion(Status status, long cents, String currency) {

    public enum Status { IDLE, LOADING, DONE, FAILED }

    public static final Conversion IDLE = new Conversion(Status.IDLE, 0, "");

    public static Conversion loading(String currency) { return new Conversion(Status.LOADING, 0, currency); }
    public static Conversion done(long cents, String currency) { return new Conversion(Status.DONE, cents, currency); }
    public static Conversion failed(String currency) { return new Conversion(Status.FAILED, 0, currency); }
}
