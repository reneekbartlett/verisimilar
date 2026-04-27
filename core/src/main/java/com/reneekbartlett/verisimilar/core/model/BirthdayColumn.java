//package com.reneekbartlett.verisimilar.core.model;
//
//import java.time.LocalDate;
//import java.util.Collection;
//import java.util.Iterator;
//import java.util.Set;
//
//import it.unimi.dsi.fastutil.ints.IntArrayList;
//import it.unimi.dsi.fastutil.ints.IntComparator;
//import tech.tablesaw.api.CategoricalColumn;
//import tech.tablesaw.api.ColumnType;
//import tech.tablesaw.api.DateColumn;
//
//public class BirthdayColumn {
//
//    public static final String HEADER_NAME = "BIRTHDAY";
//
//    private final DateColumn dateColumn;
//
//    public BirthdayColumn(DateColumn dateColumn) {
//        this.dateColumn = dateColumn;
//    }
//
//    public int getAge() {
//        
//        return 0;
//    }
//
//    /** Creates a new DateColumn with the given name and data */
//    public static BirthdayColumn create(String name, Collection<LocalDate> data) {
//      //DateColumn column = new DateColumn(name, new IntArrayList(data.size()));
//      BirthdayColumn birthdayColumn = new BirthdayColumn(DateColumn.create(name, data));
//      return birthdayColumn;
//    }
//}
