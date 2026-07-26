package com.reneekbartlett.verisimilar.core.selector;

//import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.function.Predicate;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
//import org.sqlite.jdbc4.JDBC4Connection;
//import org.sqlite.jdbc4.JDBC4PreparedStatement;

import com.reneekbartlett.verisimilar.core.model.TemplateField;
import com.reneekbartlett.verisimilar.core.selector.filter.SelectionFilter;

public class SelectionQuery {

    private static final Logger LOGGER = LoggerFactory.getLogger(SelectionQuery.class);

    public String sql;
    public List<Object> params;
    public int limit;

    public SelectionQuery() {
        limit = 1;
    }

    @Override
    public String toString() {
        return sql + " \n SelectionQuery=" + Arrays.toString(params.toArray());
    }

    static class MapCheckContext {
        Map<TemplateField, String> map;
        String sqlOperator;
        Function<Object, Object> valueTransformer;
        Boolean returnWhenFound;

        private MapCheckContext(Map<TemplateField, String> map, String sqlOperator, Function<Object, Object> valueTransformer) {
            this.map = map;
            this.sqlOperator = sqlOperator;
            this.valueTransformer = valueTransformer;
            this.returnWhenFound = true;
        }

        private MapCheckContext(Map<TemplateField, String> map, String sqlOperator, Function<Object, Object> valueTransformer, Boolean returnWhenFound) {
            this.map = map;
            this.sqlOperator = sqlOperator;
            this.valueTransformer = valueTransformer;
            this.returnWhenFound = returnWhenFound;
        }
    }

    public static SelectionQuery fromSelectionFilter(SelectionFilter filter, TemplateField field) {
        String tableName = getTableNameForField(field);

        if(tableName.isBlank()) {
            // todo: throw exception
            return null;
        }

        StringBuilder sql = new StringBuilder("SELECT ").append(field.name())
                .append(" FROM ").append(tableName)
                .append(" WHERE 1=1");
        List<Object> params = new ArrayList<>();

        // Returns true when key is found, false to continue checking next maps
        Predicate<MapCheckContext> checkAndAppend = ctx -> {
            if (ctx.map.containsKey(field)) {
                Object value = ctx.map.get(field);
                sql.append(" AND ").append(field.name()).append(ctx.sqlOperator);
                params.add(ctx.valueTransformer.apply(value));
                return ctx.returnWhenFound;
            }
            return false;
        };

        // Execute the chain in order of priority
        // First check exact match map, then partial match map (fixed to pull value from containsMap)
        // Add startsWith ands endsWith if set
        boolean processedPt1 = checkAndAppend.test(new MapCheckContext(filter.equalToMap(), " = ?", v -> v)) || 
            checkAndAppend.test(new MapCheckContext(filter.containsMap(), " LIKE ?", v -> "%" + v + "%", false)) ||
            checkAndAppend.test(new MapCheckContext(filter.startsWithMap(), " LIKE ?", v -> "" + v + "%", false)) || 
            checkAndAppend.test(new MapCheckContext(filter.endsWithMap(), " LIKE ?", v -> "%" + v + "", false));
        LOGGER.debug("processedPt1={}", processedPt1);

        SelectionQuery q = new SelectionQuery();
        sql.append(" ORDER BY RANDOM()");
        sql.append(" LIMIT " + String.valueOf(q.limit));

        q.sql = sql.toString();
        q.params = params;

        return q;
    }

    private static String getTableNameForField(TemplateField field) {
        return switch (field.name()) {
            case "STREET_NAME" -> "street_names_us";
            default -> ""; // TODO: put generic word table here.
        };
    }
}
