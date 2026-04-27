// package com.reneekbartlett.verisimilar.core.model;

// import java.util.stream.IntStream;

// import org.slf4j.Logger;
// import org.slf4j.LoggerFactory;

// import tech.tablesaw.api.Table;
// import tech.tablesaw.columns.Column;

// //public class CsvTable extends CsvRelation implements Iterable<CsvRow> {
// public class CsvTable extends Table {

//     @SuppressWarnings("unused")
//     private static final Logger LOGGER = LoggerFactory.getLogger(CsvTable.class);

//     private final Table table;
//     private final int rowCount;
//     private final int columnCount;
//     private final String[] headers;

//     public CsvTable(Table table, String[] headers) {
//         super(table.name(), table.columns());
//         this.table = table;
//         this.rowCount = table.rowCount();
//         this.columnCount = table.columnCount();
//         this.headers = headers;
//     }

//     public CsvTable(Table table) {
//         super(table.name(), table.columns());
//         this.table = table;
//         this.rowCount = table.rowCount();
//         this.columnCount = table.columnCount();
//         this.headers = getHeaderTokens(table);
//     }

//     private CsvTable(Builder b){
//         super(b.table.name(), b.table.columns());
//         this.table = b.table;
//         this.headers = b.headers;
//         this.rowCount = b.table.rowCount();
//         this.columnCount = b.table.columnCount();
//     }

//     public ValueData<String, ValueCount[]> valueData(String columnName) {
//         var valueCounts = new ValueCount[rowCount];

//         final String[][] data = getDataTokens();
//         int rowIndex = 0;
//         for (String[] row : data) {
//             valueCounts[rowIndex] = new ValueCount(row[0], Integer.valueOf(row[1]));
//             rowIndex++;
//         }
//         //TODO:  var dfWriter = table.write().asJson();
//         return new ValueData<String, ValueCount[]>(columnName, valueCounts);
//     }

//     private String[] getHeaderTokens(Table frame) {
//         final String[] header = new String[frame.columnCount()];
//         IntStream.range(0, header.length).forEach(colIndex -> { header[colIndex] = frame.column(colIndex).name(); });
//         return header;
//     }

//     public String[] getHeaders() {
//         return headers;
//     }

//     public int getColumnCount() {
//         return columnCount;
//     }

//     private String[][] getDataTokens() {
//         if (table.rowCount() == 0) return new String[0][0];
//         final int colCount = table.columnCount();
//         final String[][] data = new String[rowCount][colCount];
//         for (int i = 0; i < rowCount; i++) {
//             for (int j = 0; j < colCount; j++) {
//                 Column<?> col = table.column(j);
//                 String value = getDataToken(col, i);
//                 data[i][j] = value == null ? "" : value;
//             }
//         }
//         return data;
//     }

//     private static String getDataToken(Column<?> col, int i) {
//         return col.size() > i ? col.getString(i) : "?"; //TOO_SHORT_COLUMN_MARKER;
//     }

//     public static Builder builder() { return new Builder(); }

//     public static final class Builder {
//         private Table table;
//         private String[] headers;

//         private Builder() {}

//         public Builder table(Table table) { this.table = table; return this; }
//         public Builder headers(String[] headers) { this.headers = headers; return this; }

//         public CsvTable build() { return new CsvTable(this); }
//     }

// }
