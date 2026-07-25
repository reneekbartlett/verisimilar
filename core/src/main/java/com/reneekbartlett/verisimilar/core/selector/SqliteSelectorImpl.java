package com.reneekbartlett.verisimilar.core.selector;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.reneekbartlett.verisimilar.core.model.TemplateField;
import com.reneekbartlett.verisimilar.core.selector.filter.SelectionFilter;

public class SqliteSelectorImpl<T> implements RandomSelector<T> {

    private static final Logger LOGGER = LoggerFactory.getLogger(SqliteSelectorImpl.class);

    private static final String URL = "jdbc:sqlite:C:/S3/netsyms/AddressDatabase2025-lite-onlyplus4/scratch/AddressDatabase2025-lite-onlyplus4.sqlite";

    private final Class<T> type;
    private final List<String> dataset;
    private final TemplateField field;
    private final int valueCount;

    private volatile SelectionFilter filter;

    // memoization cache
    //private final ConcurrentMap<SelectionFilter, UniformSelectorImpl<T>> filteredCache = new ConcurrentHashMap<>();

    public SqliteSelectorImpl(Class<T> type) {
        this.type = type;
        this.field = TemplateField.STREET_NAME;
        this.dataset = List.of();
        this.valueCount = 0;
    }

    public SqliteSelectorImpl(TemplateField field, Class<T> type) {
        Objects.requireNonNull(field, "TemplateField");
        this.field = field;
        this.type = type;
        this.dataset = List.of();
        this.valueCount = 0;
    }

    @Override
    public T select() {
        SelectionFilter filter = this.filter;
        if (filter != null && !filter.isEmpty()) {
            //UniformSelectorImpl<T> filtered = filteredCache.computeIfAbsent(filter, this::buildFilteredSelector);
            //return filtered.select();
            return selectFilteredData(filter);
        }
        // select un-filtered
        return selectUnfilteredData();

        //int idx = ThreadLocalRandom.current().nextInt(valueCount);
        //return dataset.get(idx);
    }

    private T selectUnfilteredData() {
        String sql = "SELECT street_name FROM street_names_us ORDER BY RANDOM() LIMIT 1";
        try (Connection conn = DriverManager.getConnection(URL);
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery(sql)) {
            // if bc query is limit 1
            if (rs.next()) {
                return getColumnValue(rs, "street_name", this.type);
            }
        } catch (SQLException e) {
            LOGGER.error("Query failed: " + e.getMessage());
        }
        return null;
    }

    private static <T> T getColumnValue(ResultSet rs, String columnLabel, Class<T> type) throws SQLException {
        if (rs == null || columnLabel == null || type == null) {
            throw new IllegalArgumentException("Arguments cannot be null");
        }
        return rs.getObject(columnLabel, type);
    }

    private T selectFilteredData(SelectionFilter filter) {
        // TODO:  QUERY BUILDER
        //String sql = "SELECT street_name FROM street_names_us ORDER BY RANDOM() LIMIT 1";
        //String sql = "SELECT street_name FROM street_name WHERE street_name LIKE ? ORDER BY RANDOM() LIMIT 1";

        String sql = SelectionQuery.fromSelectionFilter(filter);

        //PreparedStatement
        try (Connection conn = DriverManager.getConnection(URL);
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery(sql)) {
            // if bc query is limit 1
            if (rs.next()) {
                return getColumnValue(rs, "street_name", this.type);
            }
        } catch (SQLException e) {
            LOGGER.error("Query failed: " + e.getMessage());
        }
        return null;
    }

    @Override
    public int getValueCount() {
        return this.valueCount;
    }

    public void setFilter(SelectionFilter filter) {
        this.filter = filter;
    }

    private class SelectionQuery {
        public static String fromSelectionFilter(SelectionFilter filter, TemplateField field) {
            StringBuilder sql = new StringBuilder("SELECT street_name FROM street_name WHERE 1=1");
            List<Object> params = new ArrayList<>();

            if(filter.equalToMap().containsKey(field)) {
                sql.append(" AND street_name = ?");
                params.add(filter.equalToMap().get(field));
            }

            if(filter.containsMap().containsKey(field)) {
                sql.append(" AND name LIKE ?");
                params.add("%" + filter.equalToMap().get(field) + "%"); 
            }

            if(filter.startsWithMap().containsKey(field)) {
                sql.append(" AND name LIKE ?");
                params.add(filter.equalToMap().get(field) + "%");
            }

            sql.append("ORDER BY RANDOM() LIMIT 1");

            return sql.toString();
        }
    }
}
