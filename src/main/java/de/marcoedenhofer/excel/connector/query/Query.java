package de.marcoedenhofer.excel.connector.query;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;

import java.util.ArrayList;
import java.util.IdentityHashMap;
import java.util.List;
import java.util.Map;
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

        // remove all keys, which are of no interest for the client
        columnNamesToIndexMap.forEach((key, value) -> {
            if (selectedColumns.stream().noneMatch(name -> name.equalsIgnoreCase(key))) {
                columnNamesToIndexMap.remove(key);
            }
        });

        return columnNamesToIndexMap;
    }

    private Map<String, Integer> extractColumnNames(Sheet sheet) {
        Map<String, Integer> columnNamesToIndexMap = new IdentityHashMap<>();

        Row firstRow = sheet.getRow(sheet.getFirstRowNum());
        firstRow.cellIterator().forEachRemaining(cell -> {
            columnNamesToIndexMap.put(cell.getStringCellValue(),cell.getColumnIndex());
        });

        return columnNamesToIndexMap;
    }

    private QueryResult buildQueryResult(List<Map<String, String>> results) {
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
