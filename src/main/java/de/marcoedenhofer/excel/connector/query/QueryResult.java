package de.marcoedenhofer.excel.connector.query;

import lombok.AllArgsConstructor;

import java.util.List;
import java.util.Map;

@AllArgsConstructor
public class QueryResult {
    private final List<String> columns;
    private final List<Map<String, String>> resultRows;

    public List<String> getColumns() {
        return List.copyOf(columns);
    }

    public List<Map<String, String>> getResultRows() {
        return List.copyOf(resultRows);
    }
}
