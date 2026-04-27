//package com.reneekbartlett.verisimilar.api.service;
//
//import org.springframework.stereotype.Service;
//
//import com.reneekbartlett.verisimilar.core.model.CsvTable;
//import com.reneekbartlett.verisimilar.core.model.FieldMap;
////import com.reneekbartlett.verisimilar.model.ValueCount;
//
//import java.time.LocalDate;
//import java.time.format.DateTimeFormatter;
//import java.time.temporal.ChronoUnit;
////import java.util.Arrays;
//
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//
//import tech.tablesaw.aggregate.Summarizer;
//import tech.tablesaw.api.ColumnType;
//import tech.tablesaw.api.DateColumn;
//import tech.tablesaw.api.IntColumn;
////import tech.tablesaw.api.Row;
//import tech.tablesaw.api.Table;
//import tech.tablesaw.columns.Column;
//import tech.tablesaw.io.csv.CsvReadOptions;
//
//import tech.tablesaw.plotly.Plot;
//import tech.tablesaw.plotly.api.HorizontalBarPlot;
//import tech.tablesaw.plotly.components.Figure;
//import tools.jackson.databind.JsonNode;
//import tools.jackson.databind.node.JsonNodeFactory;
//import tools.jackson.databind.node.ObjectNode;
//
//import static tech.tablesaw.aggregate.AggregateFunctions.*;
//
//@Service
//public class AnalysisService {
//
//    private static final Logger LOGGER = LoggerFactory.getLogger(AnalysisService.class);
//
//    public AnalysisService() {
//        //
//    }
//
//    public String getAnalysis(String file, String[] columnMap) {
//
//        // TODO:  Read .map file
//
//        String[] columnNames = new String[columnMap.length];
//        ColumnType[] columnTypes = new ColumnType[columnMap.length];
//        for(String columnInfo : columnMap) {
//            String[] v = columnInfo.split(":");
//            if(v.length < 2 || v.length > 3) {
//                throw new IllegalArgumentException("Invalid column field map.");
//            }
//            int columnIndex = Integer.valueOf(v[0]);
//            columnNames[columnIndex] = v[1];
//            columnTypes[columnIndex] = ColumnType.valueOf(v[2]);
//        }
//
//        var fieldMap = new FieldMap<>(columnNames, columnTypes);
//        boolean hasHeader = false;
//
//        String fileData;
//        try {
//            fileData = getTablesawData(file, fieldMap, hasHeader);
//        } catch (Exception e) {
//            fileData = "";
//            LOGGER.error("Error analyzing file: " + file, e);
//        }
//        return fileData;
//    }
//
//    public static String getTablesawData(String file, FieldMap fieldMap, boolean hasHeader) throws IllegalArgumentException {
//        CsvReadOptions csvReadOptions = CsvReadOptions.builder(file)//.separator(',')
//                .dateFormat(DateTimeFormatter.ISO_LOCAL_DATE)
//                .columnTypes(fieldMap.columnTypes())
//                .header(hasHeader)
//                .build();
//        Table table = Table.read().usingOptions(csvReadOptions);
//        if(!hasHeader) {
//            for(int i = 0; i < fieldMap.columnNames().length; i++) {
//                table.column(i).setName(fieldMap.columnNames()[i]);
//            }
//        }
//
//        int totalRecords = table.rowCount();
//
//        ObjectNode resultNodes = JsonNodeFactory.instance.objectNode();
//
//        resultNodes.put("totalRecords", totalRecords);
//
//        // TODO:  First get data by column index, doesn't matter what it's mapped to.
//        for (Column<?> col : table.columns()) {
//
//            if(col.name().equals("phoneNumber")) {
//                continue;
//            }
//
//            Table topValues = getTop(table, col, 10);
//            //LOGGER.info(topValues.print());
//
//            var testCsvTable = new CsvTable(topValues);
//            JsonNode topNode = testCsvTable.valueData(col.name()).toJsonNode();
//            resultNodes.putIfAbsent("TOP_"+col.name().toUpperCase(), topNode);
//            //LOGGER.info(node.toString());
//
//            Table uniqueValues = getUniqueCounts(table,col);
//            LOGGER.info(uniqueValues.print());
//        }
//
//        // BIRTHDAY
//        if(table.containsColumn("birthday")) {
//            DateColumn birthdayCol = table.dateColumn("birthday");
//            IntColumn birthYearCol = birthdayCol.year();
//            birthYearCol.setName("BIRTH_YEAR");
//            table.addColumns(birthYearCol);
//
//            IntColumn ageColumn = birthdayCol.map(bday -> (int) ChronoUnit.YEARS.between(bday, LocalDate.now()), IntColumn::create);
//            ageColumn.setName("AGE");
//            table.addColumns(ageColumn);
//
//            //Table summary = table.summarize("birthday", latestDate, earliestDate).apply();
//            //Table yearCounts = table.summarize("BIRTH_YEAR", count).by("BIRTH_YEAR");
//
////          String[] b = new String[summary.rowCount()];
////          String youngest;
////          String oldest;
////          if(summary.rowCount() == 1) {
////              Row row = summary.row(0);
////              youngest = row.getDate(0).toString();
////              oldest = row.getDate(1).toString();
////          } else {
////              youngest = "n/a";
////              oldest = "n/a";
////          }
////          LOGGER.info("done \t youngest=" + youngest + ", oldest=" + oldest);
//        }
//        //LOGGER.info(table.print());
//
//        return resultNodes.toString();
//    }
//
//    public static Table getTop(Table table, Column column, int top) {
//        // First get the frequency table, then sort by count (descending) and take the top X
//        Table counts = switch(column.type().name()) {
//            case "LOCAL_DATE" -> table.dateColumn(column.name()).countByCategory();
//            case "STRING" -> table.stringColumn(column.name()).countByCategory();
//            default -> table.stringColumn(column.name()).countByCategory();
//        };
//        return counts.sortDescendingOn("Count").first(top);
//    }
//
//    public static Table getBottom(Table table, Column column, int rowCount) {
//        // First get the frequency table, then sort by count (descending) and take the top X
//        Table counts = switch(column.type().name()) {
//            case "LOCAL_DATE" -> table.dateColumn(column.name()).countByCategory();
//            case "STRING" -> table.stringColumn(column.name()).countByCategory();
//            default -> table.stringColumn(column.name()).countByCategory();
//        };
//        return counts.sortDescendingOn("Count").last(rowCount);
//    }
//
//    public static Table getUniqueCounts(Table table, Column column) {
//        Summarizer summarizer = switch(column.type().name()) {
//            case "STRING" -> table.summarize(column, countUnique, countMissing);
//            case "INTEGER" -> table.summarize(column, countUnique, countMissing);
//            default -> table.summarize(column, countUnique, countMissing);
//        };
//        return summarizer.apply();
//    }
//
//    private Figure getTablesawFigure(Table tbl) {
//        Figure figure = HorizontalBarPlot.create("Title", tbl, "scale", "sum [fatalities]");
//
//        Plot.show(figure);
//        return figure;
//    }
//
//}
