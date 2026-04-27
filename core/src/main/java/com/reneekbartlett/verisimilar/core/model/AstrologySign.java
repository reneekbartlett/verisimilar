package com.reneekbartlett.verisimilar.core.model;

import java.time.LocalDate;

public enum AstrologySign {
    ARIES("Aries", "March 21", "April 19"),
    TAURUS("Taurus", "April 20", "May 20"),
    GEMINI("Gemini", "May 21", "June 20"),
    CANCER("Cancer", "June 21", "July 22"),
    LEO("Leo", "July 23", "August 22"),
    VIRGO("Virgo", "August 23", "September 22"),
    LIBRA("Libra", "September 23", "October 22"),
    SCORPIO("Scorpio", "October 23", "November 21"),
    SAGITTARIUS("Sagittarius", "November 22", "December 21"),
    CAPRICORN("Capricorn", "December 22", "January 19"),
    AQUARIUS("Aquarius", "January 20", "February 18"),
    PISCES("Pisces", "February 19", "March 20");

    private final String displayName;
    private final String start;
    private final String end;

    private AstrologySign(String displayName, String start, String end) {
        this.displayName = displayName;
        this.start = start;
        this.end = end;
    }

    public String getDisplayName() {
        return displayName;
    }

    public String getRange() {
        return start + " – " + end;
    }

    @Override
    public String toString() {
        return displayName + " (" + getRange() + ")";
    }

    public static AstrologySign fromName(String name) {
        for (AstrologySign sign : values()) {
            if (name.equalsIgnoreCase(sign.displayName)) {
                return sign;
            }
        }
        throw new IllegalArgumentException("Unrecognized Astrology name:" + name);
    }

    public static AstrologySign fromLocalDate(LocalDate date) {
        int month = date.getMonthValue();
        int day = date.getDayOfMonth();

        return switch (month) {
            case 1  -> (day < 20) ? AstrologySign.CAPRICORN : AstrologySign.AQUARIUS;
            case 2  -> (day < 19) ? AstrologySign.AQUARIUS : AstrologySign.PISCES;
            case 3  -> (day < 21) ? AstrologySign.PISCES : AstrologySign.ARIES;
            case 4  -> (day < 20) ? AstrologySign.ARIES : AstrologySign.TAURUS;
            case 5  -> (day < 21) ? AstrologySign.TAURUS : AstrologySign.GEMINI;
            case 6  -> (day < 21) ? AstrologySign.GEMINI : AstrologySign.CANCER;
            case 7  -> (day < 23) ? AstrologySign.CANCER : AstrologySign.LEO;
            case 8  -> (day < 23) ? AstrologySign.LEO : AstrologySign.VIRGO;
            case 9  -> (day < 23) ? AstrologySign.VIRGO : AstrologySign.LIBRA;
            case 10 -> (day < 23) ? AstrologySign.LIBRA : AstrologySign.SCORPIO;
            case 11 -> (day < 22) ? AstrologySign.SCORPIO : AstrologySign.SAGITTARIUS;
            case 12 -> (day < 22) ? AstrologySign.SAGITTARIUS : AstrologySign.CAPRICORN;
            default -> throw new IllegalArgumentException("Invalid month: " + month);
        };
    }

}
