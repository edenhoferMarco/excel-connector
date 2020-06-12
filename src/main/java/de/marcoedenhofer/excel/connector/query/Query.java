package de.marcoedenhofer.excel.connector.query;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;

import java.util.*;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.stream.Collectors;

@Slf4j
@Getter(AccessLevel.MODULE)
public class Query {
    private final List<String> selectedColumns;
    private final Supplier<Sheet> sheetSupplier;
    private final boolean selectAllColumns;

    Query(Supplier<Sheet> sheetSupplier, QueryColumnSelector columnSelector) {
        this.sheetSupplier = sheetSupplier;
        this.selectedColumns = columnSelector.getColumns();
        this.selectAllColumns = columnSelector.selectAllColumns();
    }

    public QueryResult execute() {
        Sheet sheet = getSheetFromSupplier();
        Map<String, Integer> namesToIndex;

        if (selectAllColumns) {
            namesToIndex = findIndexForAllColumns(sheet);
        } else {
            namesToIndex = findIndexForSelectedColumns(sheet);
        }

        List<Map<String, String>> results = new ArrayList<>();

        sheet.rowIterator()
                .forEachRemaining(row -> results.add(
                        namesToIndex.entrySet().stream()
                                .collect(Collectors.toMap(
                                        Map.Entry::getKey,
                                        entry -> transformCellValueToString(row.getCell(entry.getValue())))
                                )
                ));

        return buildQueryResult(results);
    }

    private Map<String, Integer> findIndexForAllColumns(Sheet sheet) {
        return extractColumnNames(sheet);
    }

    private Map<String, Integer> findIndexForSelectedColumns(Sheet sheet) {
        Map<String, Integer> columnNamesToIndexMap = extractColumnNames(sheet);
        final Predicate<Map.Entry<String, Integer>> isSelectedColumn =
                entry -> selectedColumns
                        .stream()
                        .anyMatch(selectedColName ->
                                selectedColName.equalsIgnoreCase(entry.getKey()));

        // remove all keys, which are of no interest for the client
        return columnNamesToIndexMap.entrySet()
                .stream()
                .filter(isSelectedColumn)
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    }

    private Map<String, Integer> extractColumnNames(Sheet sheet) {
        Map<String, Integer> columnNamesToIndexMap = new HashMap<>();

        Row firstRow = sheet.getRow(sheet.getFirstRowNum());
        if (firstRow != null) {
            firstRow.cellIterator().forEachRemaining(cell -> {
                columnNamesToIndexMap.put(cell.getStringCellValue(),cell.getColumnIndex());
            });
        } else {
            log.info("Seems the sheet: {} is not populated", sheet.getSheetName());
        }

        return columnNamesToIndexMap;
    }

    private QueryResult buildQueryResult(List<Map<String, String>> results) {
        if (results.isEmpty()) {
            log.info("No results found, returning empty QueryResult");
            return new QueryResult(Collections.emptyList(),Collections.emptyList());
        }
        return new QueryResult(results.get(0).keySet().stream().collect(Collectors.toUnmodifiableList()),
                results.subList(1,results.size()));
    }

    private Sheet getSheetFromSupplier() {
        Sheet sheet = sheetSupplier.get();
        if (sheet == null) {
            log.error("Specified Sheet could not be found!");
            throw new IllegalArgumentException("Specified Sheet could not be found!");
        }

        return sheet;
    }

    private String transformCellValueToString(Cell cell) {
        switch (cell.getCellTypeEnum()) {
            case STRING: return cell.getStringCellValue();
            case NUMERIC: return Double.toString(cell.getNumericCellValue());
            case BOOLEAN: return Boolean.toString(cell.getBooleanCellValue());
            default: return "INVALID INPUT";
        }
    }
}
