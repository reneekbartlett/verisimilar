// package com.reneekbartlett.verisimilar.core.model;

// import tech.tablesaw.api.Table;
// import tech.tablesaw.columns.Column;
// //import tech.tablesaw.table.Relation;

// public class FakeFileSummary {
//     private final String uuid;
//     private final long lineCount;

//     // UNIQUE
//     //private int uniqueFirstNameCount;
//     //private int uniqueLastNameCount;
//     //private int uniqueFullNameCount;

//     public FakeFileSummary(String uuid, long lineCount) {
//         this.uuid = uuid;
//         this.lineCount = lineCount;
//     }

//     public String getUuid() {
//         return this.uuid;
//     }

//     public long getLineCount() {
//         return this.lineCount;
//     }

//     // Table extends Relation
//     public static String[][] getDataArray(Table table, int rowCount, int colCount){
//         String[][] data = new String[rowCount][colCount];
//         for (int i = 0; i < rowCount; i++) {
//           for (int j = 0; j < colCount; j++) {
//             Column<?> col = table.column(j);
//             String value = getDataToken(col, i);
//             data[i][j] = value == null ? "" : value;
//           }
//         }
//         return data;
//     }

//     private static String getDataToken(Column<?> col, int i) {
//         return col.size() > i ? col.getString(i) : "?"; //TOO_SHORT_COLUMN_MARKER;
//     }

//     public record ColumnSummary<T1, T2, T3>(String columnName, String valueType, 
//             ValueData<String,ValueCount>[] valueData){
//         @Override
//         public String toString() {
//             return new StringBuilder().append(this.columnName).append(":").append(this.valueType).append("=").append(this.valueData.toString()).toString();
//         }
//     }

// }
