package de.marcoedenhofer.excel.connector.query;

import lombok.AccessLevel;
import lombok.Getter;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

import java.util.function.Supplier;

@Getter(AccessLevel.MODULE)
public class QueryBuilder {
    private final Workbook workbook;
    private Supplier<Sheet> sheetSupplier;
    private QueryColumnSelector queryColumnSelector;

    private QueryBuilder(Workbook workbook) {
        this.workbook = workbook;
    }

    public static QueryBuilder forWorkbook(Workbook workbook) {
        return new QueryBuilder(workbook);
    }

    public QueryBuilder fromFirstSheet() {
        return fromSheet(0);
    }

    public QueryBuilder fromSheet(int sheetNumber) {
        this.sheetSupplier = () ->  workbook.getSheetAt(sheetNumber);

        return this;
    }

    public QueryBuilder fromSheet(String sheetName) {
        this.sheetSupplier = () -> workbook.getSheet(sheetName);

        return this;
    }

    public QueryBuilder select(QueryColumnSelector columnSelector) {
        this.queryColumnSelector = columnSelector;

        return this;
    }

    /**
     * Builds the {@link Query} from the specified inputs, clears all inputs but the workbook and finally returns the
     * built {@link Query} object.
     * @return the {@link Query} to execute.
     */
    public Query build() {
        final Query query = new Query(sheetSupplier, queryColumnSelector);

        this.queryColumnSelector = null;
        this.sheetSupplier = null;

        return query;
    }
}
