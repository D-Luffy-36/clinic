package com.an.DentalClinicSystem.utils;

public class FormatUtils {
    public static String safe(String value) {
        return value != null ? value : "";
    }
}