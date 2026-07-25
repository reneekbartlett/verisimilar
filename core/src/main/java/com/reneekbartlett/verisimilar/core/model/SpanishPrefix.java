package com.reneekbartlett.verisimilar.core.model;

public enum SpanishPrefix implements WeightedEnumData {
    AVENIDA("AVE", 0.0000, "AVENIDA","AVE"), //AVENUE
    CALLE("CLL", 0.0000, "CALLE","CLL"), //STREET
    CAMINITO("CMT", 0.0000, "CAMINITO","CMT"), // LITTLE ROAD
    CAMINO("CAM", 0.0000, "CAMINO","CAM"), // ROAD
    CERRADA("CER", 0.0000, "CERRADA","CER"), // CLOSED
    CIRCULO("CIR", 0.0000, "CIRCULO","CIR"), //CIRCLE
    ENTRADA("ENT", 0.0000, "ENTRADA","ENT"), //ENTRANCE
    PASEO("PSO", 0.0000, "PASEO","PSO"), //PATH
    PLACITA("PLA", 0.0000, "PLACITA","PLA"), // LITTLE PLAZA
    RANCHO("RCH", 0.0000, "RANCHO","RCH"), // RANCH
    VEREDA("VER", 0.0000, "VEREDA","VER"), // SMALL PATH
    VISTA("VIS", 0.0000, "VISTA","VIS"), // VIEW
    NONE("", 0.0000, " ");

    private final String label;
    private final double weight;
    private final String[] searchStr;

    private SpanishPrefix(String label, double weight, String... searchStr) {
        this.label = label; // usps standard
        this.weight = weight;
        this.searchStr = searchStr;
    }

    public String getLabel() {
        return label;
    }

    public double getWeight() {
        return weight;
    }

    public String[] getSearchStrings() {
        return searchStr;
    }
}
