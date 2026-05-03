package com.reneekbartlett.verisimilar.core.model;

import java.util.Arrays;
import java.util.EnumSet;
import java.util.List;
import java.util.stream.Collectors;

public enum FullNamePrefix {
    // Social
    MR("Mr.", Category.SOCIAL, "Mr"),
    MRS("Mrs.", Category.SOCIAL, "Mrs"),
    MS("Ms.", Category.SOCIAL, "Ms"),
    MISS("Miss", Category.SOCIAL),
    MX("Mx.", Category.SOCIAL, "Mx"),

    // Professional & Academic
    DR("Dr.", Category.PROFESSIONAL, "Dr", "Doctor"),
    PROF("Prof.", Category.PROFESSIONAL, "Prof", "Professor"),
    REV("Rev.", Category.PROFESSIONAL, "Rev", "Reverend"),
    HON("Hon.", Category.PROFESSIONAL, "Hon", "Honorable"),

    // Military
    GEN("Gen.", Category.MILITARY, "General"),
    COL("Col.", Category.MILITARY, "Colonel"),
    MAJ("Maj.", Category.MILITARY, "Major"),
    CAPT("Capt.", Category.MILITARY, "Captain"),
    LIEUT("Lt.", Category.MILITARY, "Lt", "Lieutenant"),
    SGT("Sgt.", Category.MILITARY, "Sgt", "Sergeant");

    public enum Category {
        SOCIAL, PROFESSIONAL, MILITARY
    }

    private final String displayName;
    private final Category category;
    private final String[] aliases;

    FullNamePrefix(String displayName, Category category, String... aliases) {
        this.displayName = displayName;
        this.category = category;
        this.aliases = aliases;
    }

    public String getDisplayName() {
        return displayName;
    }

    public Category getCategory() {
        return category;
    }

    /**
     * Matches input against enum name, display name, or aliases (case-insensitive).
     * Handles "Dr", "Dr.", "DOCTOR", etc.
     */
    public static FullNamePrefix fromString(String text) {
        if (text == null || text.isBlank()) return null;

        String cleanText = text.trim().replace(".", "");

        for (FullNamePrefix prefix : EnumSet.allOf(FullNamePrefix.class)) {
            if (prefix.name().equalsIgnoreCase(cleanText)) return prefix;
            if (prefix.displayName.replace(".", "").equalsIgnoreCase(cleanText)) return prefix;
            for (String alias : prefix.aliases) {
                if (alias.replace(".", "").equalsIgnoreCase(cleanText)) return prefix;
            }
        }
        return null;
    }

    /**
     * Extracts a prefix from the start of a full name.
     * Example: "Dr. Jane Doe" returns NamePrefix.DR
     */
    public static FullNamePrefix findInName(String fullName) {
        if (fullName == null || fullName.isBlank()) return null;
        String firstWord = fullName.trim().split("\\s+")[0];
        return fromString(firstWord);
    }

    public static List<FullNamePrefix> getByCategory(Category category) {
        return Arrays.stream(values())
                .filter(p -> p.category == category)
                .collect(Collectors.toList());
    }

    @Override
    public String toString() {
        return displayName;
    }
}
