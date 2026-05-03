package com.reneekbartlett.verisimilar.core.model;

import java.util.Arrays;
import java.util.EnumSet;
import java.util.List;
import java.util.stream.Collectors;

public enum FullNameSuffix {
    JR("Jr.", Category.GENERATIONAL, "Jr", "Junior"),
    SR("Sr.", Category.GENERATIONAL, "Sr", "Senior"),
    I("I", Category.GENERATIONAL, "First"),
    II("II", Category.GENERATIONAL, "Second"),
    III("III", Category.GENERATIONAL, "Third", "3"),
    IV("IV", Category.GENERATIONAL),
    V("V", Category.GENERATIONAL),

    // Professional & Academic
    MD("M.D.", Category.PROFESSIONAL, "MD"),
    PHD("PH.D.", Category.PROFESSIONAL, "PHD"),
    JD("J.D.", Category.PROFESSIONAL, "JD"),
    ESQ("ESQ.", Category.PROFESSIONAL, "ESQ"),
    RN("R.N.", Category.PROFESSIONAL, "RN"),
    DO("D.O.", Category.PROFESSIONAL, "DO"),
    DDS("D.D.S.", Category.PROFESSIONAL, "DDS"),
    DVM("D.V.M.", Category.PROFESSIONAL, "DVM"),
    EDD("Ed.D.", Category.PROFESSIONAL, "EDD"),
    PE("P.E.", Category.PROFESSIONAL, "PE"),

    // Military
    RET("Ret.", Category.MILITARY, "Retired"),
    USA("USA", Category.MILITARY, "U.S. Army"),
    USAF("USAF", Category.MILITARY, "U.S. Air Force"),
    USN("USN", Category.MILITARY, "U.S. Navy"),
    USMC("USMC", Category.MILITARY, "U.S. Marine Corps"),
    USCG("USCG", Category.MILITARY, "U.S. Coast Guard");

    public enum Category {
        GENERATIONAL, PROFESSIONAL, MILITARY
    }

    private final String displayName;
    private final Category category;
    private final String[] aliases;

    FullNameSuffix(String displayName, Category category, String... aliases) {
        this.displayName = displayName;
        this.category = category;
        this.aliases = aliases;
    }

    public String getDisplayName() {
        return displayName;
    }

    public boolean isGenerational() {
        return this.category == Category.GENERATIONAL;
    }

    public boolean isProfessional() {
        return this.category == Category.PROFESSIONAL;
    }

    /**
     * Finds a suffix by its display name (e.g., "Jr.") or enum name (e.g., "JR").
     */
    public static FullNameSuffix fromString(String text) {
        if (text == null || text.isBlank()) return null;
        
        String cleanText = text.trim();
        for (FullNameSuffix suffix : EnumSet.allOf(FullNameSuffix.class)) {
            // Check Enum Name (JR)
            if (suffix.name().equalsIgnoreCase(cleanText)) return suffix;
            // Check Display Name (Jr.)
            if (suffix.displayName.equalsIgnoreCase(cleanText)) return suffix;
            // Check Aliases (Jr, Junior)
            for (String alias : suffix.aliases) {
                if (alias.equalsIgnoreCase(cleanText)) return suffix;
            }
        }
        return null;
    }

    public static List<FullNameSuffix> getByCategory(Category category) {
        return Arrays.stream(values())
                .filter(s -> s.category == category)
                .collect(Collectors.toList());
    }

    @Override
    public String toString() {
        return displayName;
    }
    
    /**
     * Extracts and returns the NameSuffix from the end of a full name string.
     * Example: "John Doe Jr." returns NameSuffix.JR
     */
    public static FullNameSuffix findInName(String fullName) {
        if (fullName == null || fullName.isBlank()) return null;

        String[] parts = fullName.trim().split("\\s+|,\\s*");
        if (parts.length < 2) return null;

        // Check the last part of the name
        return fromString(parts[parts.length - 1]);
    }

    /**
     * Checks if a string ends with any valid suffix.
     */
    public static boolean hasSuffix(String fullName) {
        return findInName(fullName) != null;
    }
}
