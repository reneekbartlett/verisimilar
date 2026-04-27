package com.reneekbartlett.verisimilar.core.model;

public enum Occupation {

    MANAGEMENT("Management Occupations"),
    BUSINESS_FINANCIAL("Business and Financial Operations Occupations"),
    COMPUTER_MATH("Computer and Mathematical Occupations"),
    ARCHITECTURE_ENGINEERING("Architecture and Engineering Occupations"),
    LIFE_PHYSICAL_SOCIAL_SCIENCE("Life, Physical, and Social Science Occupations"),
    COMMUNITY_SOCIAL_SERVICE("Community and Social Service Occupations"),
    LEGAL("Legal Occupations"),
    EDUCATION_LIBRARY("Educational Instruction and Library Occupations"),
    ARTS_DESIGN_ENTERTAINMENT_MEDIA("Arts, Design, Entertainment, Sports, and Media Occupations"),
    HEALTHCARE_PRACTITIONERS("Healthcare Practitioners and Technical Occupations"),
    HEALTHCARE_SUPPORT("Healthcare Support Occupations"),

    PROTECTIVE_SERVICE("Protective Service Occupations"),
    FOOD_PREPARATION("Food Preparation and Serving Related Occupations"),
    BUILDING_GROUNDS_MAINTENANCE("Building and Grounds Cleaning and Maintenance Occupations"),
    PERSONAL_CARE_SERVICE("Personal Care and Service Occupations"),

    SALES("Sales and Related Occupations"),
    OFFICE_ADMIN("Office and Administrative Support Occupations"),

    FARMING_FISHING_FORESTRY("Farming, Fishing, and Forestry Occupations"),
    CONSTRUCTION_EXTRACTION("Construction and Extraction Occupations"),
    INSTALLATION_MAINTENANCE_REPAIR("Installation, Maintenance, and Repair Occupations"),
    PRODUCTION("Production Occupations"),
    TRANSPORTATION_MATERIAL_MOVING("Transportation and Material Moving Occupations"),

    MILITARY("Military Specific Occupations"),

    UNEMPLOYED("Unemployed / Not in Labor Force"),
    UNKNOWN("Unknown"),
    DECLINE_TO_ANSWER("Decline to Answer");

    private final String label;

    Occupation(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }
}

