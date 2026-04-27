// package com.reneekbartlett.verisimilar.core.model;

// import tech.tablesaw.api.ColumnType;

// public record FieldMap<T1,T2>(String[] columnNames, ColumnType[] columnTypes){

//     public static Builder builder() { return new Builder(); }

//     public static final class Builder {
//         private String[] columnNames;
//         private ColumnType[] columnTypes;
//         //private String[] columnTypeNames;

//         private Builder() {}

//         public Builder columnNames(String[] columnNames) { this.columnNames = columnNames; return this; }

//         public Builder columnTypes(ColumnType[] columnTypes) { 
//             this.columnTypes = columnTypes;
//             return this;
//         }

//         public FieldMap<String[], ColumnType[]> build() { return new FieldMap<>(this.columnNames, this.columnTypes); }
//     }

// }
