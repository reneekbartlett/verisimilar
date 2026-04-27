package com.reneekbartlett.verisimilar.core.model;

public enum EducationLevel {

    NO_SCHOOLING("No schooling completed"),
    NURSERY_TO_8TH_GRADE("Nursery to 8th grade"),
    SOME_HIGH_SCHOOL("Some high school, no diploma"),
    HIGH_SCHOOL_DIPLOMA("High school graduate or equivalent (GED)"),

    SOME_COLLEGE_NO_DEGREE("Some college, no degree"),
    ASSOCIATES_DEGREE("Associate's degree"),
    BACHELORS_DEGREE("Bachelor's degree"),
    MASTERS_DEGREE("Master's degree"),
    PROFESSIONAL_DEGREE("Professional degree (e.g., JD, MD)"),
    DOCTORATE_DEGREE("Doctorate degree"),

    UNKNOWN("Unknown"),
    DECLINE_TO_ANSWER("Decline to Answer");

    private final String label;

    private EducationLevel(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }
}
