package com.reneekbartlett.verisimilar.core.selector;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.sqlite.jdbc4.JDBC4Connection;
import org.sqlite.jdbc4.JDBC4PreparedStatement;

import com.reneekbartlett.verisimilar.core.model.TemplateField;
import com.reneekbartlett.verisimilar.core.selector.filter.SelectionFilter;

/***
 * Sqlite Implementation for obtaining data in SQLite DB.
 * @param <T>
 */
public class SqliteSelectorImpl<T> implements RandomSelector<T> {

    private static final Logger LOGGER = LoggerFactory.getLogger(SqliteSelectorImpl.class);

    private final String dbUrl;
    private final String dbFileName;

    // TODO: 
    private static final String URL = "jdbc:sqlite:C:/S3/netsyms/AddressDatabase2025-lite-onlyplus4/scratch/AddressDatabase2025-lite-onlyplus4.sqlite";
    private static final String FILE_NAME = "C:/S3/netsyms/AddressDatabase2025-lite-onlyplus4/scratch/AddressDatabase2025-lite-onlyplus4.sqlite";
    private static final Properties PROP;
    static {
        PROP = new Properties();
        PROP.setProperty("open_mode", "1"); // read_only
    }

    private final Class<T> type;
    private final List<String> dataset;
    private final TemplateField field;
    private final int valueCount;

    private volatile SelectionFilter filter;

    public SqliteSelectorImpl(String dbUrl, String dbFileName, Class<T> type) {
        this.dbUrl = dbUrl;
        this.dbFileName = dbFileName;
        this.type = type;
        this.field = TemplateField.STREET_NAME;
        this.dataset = List.of();
        this.valueCount = 0;
    }
    
    public SqliteSelectorImpl(Class<T> type) {
        this.dbUrl = URL;
        this.dbFileName = FILE_NAME;
        this.type = type;
        this.field = TemplateField.STREET_NAME;
        this.dataset = List.of();
        this.valueCount = 0;
    }

    public SqliteSelectorImpl(TemplateField field, Class<T> type) {
        Objects.requireNonNull(field, "TemplateField");
        this.dbUrl = URL;
        this.dbFileName = FILE_NAME;
        this.field = field;
        this.type = type;
        this.dataset = List.of();
        this.valueCount = 0;
    }

    @Override
    public T select() {
        SelectionFilter filter = this.filter;
        if (filter != null && !filter.isEmpty()) {
            return selectFilteredData(filter);
        }
        // select un-filtered
        return selectUnfilteredData();
    }

    private T selectUnfilteredData() {
        String sql = "SELECT street_name FROM street_names_us ORDER BY RANDOM() LIMIT 1";
        try (Connection conn = DriverManager.getConnection(this.dbUrl);
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
        var selectionQry = SelectionQuery.fromSelectionFilter(filter, this.field);
        String sql = selectionQry.sql;
        List<Object> params = selectionQry.params;

        LOGGER.debug("selectFilteredData sql={}; params={}", sql, params);

        // Alt is Connection conn = DriverManager.getConnection(URL, PROP); PreparedStatement stmt = conn.prepareStatement(sql);
        try (JDBC4Connection conn = new JDBC4Connection(this.dbUrl, this.dbFileName, PROP);
                JDBC4PreparedStatement stmt = new JDBC4PreparedStatement(conn, sql); // (SQLiteConnection, String)
        ) {
            // Add parameters to PreparedStatement
            for (int i = 0; i < params.size(); i++) {
                stmt.setObject(i + 1, params.get(i));
            }

            try (ResultSet rs = stmt.executeQuery()) {
                // rs.getMetaData();
                // LOGGER.debug("JDBC4PreparedStatement={}", stmt.toString());
                if (rs.next()) {
                    if (selectionQry.limit == 1) {
                        return getColumnValue(rs, this.field.name(), this.type);
                    } else {
                        List<T> columnVals = new ArrayList<>();
                        while (rs.next() && columnVals.size() <= selectionQry.limit) {
                            columnVals.add(getColumnValue(rs, this.field.name(), this.type));
                        }
                        // TODO: figure out what to return
                        return columnVals.getLast();
                    }
                } else {
                    stmt.clearParameters();
                    return selectUnfilteredData();
                }
            } catch (SQLException e) {
                LOGGER.error("Query failed: " + e.getMessage());
            } finally {
                conn.close();
            }
        } catch (SQLException e) {
            LOGGER.error("Connection failed: " + e.getMessage());
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

}
