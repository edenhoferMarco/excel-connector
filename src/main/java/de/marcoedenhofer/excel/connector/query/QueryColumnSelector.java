package de.marcoedenhofer.excel.connector.query;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class QueryColumnSelector {
    private final List<String> columnsToQuery;
    private final boolean selectAllColumns;

    private QueryColumnSelector(List<String> columnsToQuery) {
        this.columnsToQuery = columnsToQuery;
        this.selectAllColumns = false;
    }

    private QueryColumnSelector() {
        this.columnsToQuery = Collections.emptyList();
        this.selectAllColumns = true;
    }

    public static QueryColumnSelector column(String columnName) {
        return new QueryColumnSelector(new ArrayList<>(Collections.singletonList(columnName)));
    }

    public static QueryColumnSelector all() {
        return new QueryColumnSelector();
    }

    public QueryColumnSelector and(String columnName) {
        this.columnsToQuery.add(columnName);

        return this;
    }

    List<String> getColumns() {
        return List.copyOf(columnsToQuery);
    }

    boolean selectAllColumns() {
        return selectAllColumns;
    }
}
