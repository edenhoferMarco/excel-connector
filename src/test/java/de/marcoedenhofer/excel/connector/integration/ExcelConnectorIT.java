package de.marcoedenhofer.excel.connector.integration;

import de.marcoedenhofer.excel.connector.exceptions.UnsupportedFileTypeException;
import de.marcoedenhofer.excel.connector.query.QueryBuilder;
import de.marcoedenhofer.excel.connector.query.QueryColumnSelector;
import de.marcoedenhofer.excel.connector.query.QueryResult;
import de.marcoedenhofer.excel.connector.utils.ResourceHelper;
import de.marcoedenhofer.excel.connector.utils.ValueTransformationUtils;
import de.marcoedenhofer.excel.connector.workbook.WorkbookFromFileTypeFactory;
import org.apache.poi.ss.usermodel.Workbook;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

public class ExcelConnectorIT {
    private final ResourceHelper resourceHelper = new ResourceHelper();

    @Test
    public void mapStringToInteger() throws UnsupportedFileTypeException, IOException {
        WorkbookFromFileTypeFactory factory = new WorkbookFromFileTypeFactory();
        Workbook workbook = factory.fromFile(new File(resourceHelper.getXlsxTestFilePath()));

        QueryResult result = QueryBuilder.forWorkbook(workbook)
                .fromSheet(2)
                .select(QueryColumnSelector.column("Age"))
                .build()
                .execute();

        List<Map<String, String>> rows = result.getResultRows()
                .stream()
                .peek(row -> row.put("Age", ValueTransformationUtils.transformToInteger(row.get("Age")).toString()))
                .collect(Collectors.toUnmodifiableList());

        assertThat(rows.get(0).get("Age"), is("32"));
    }

    @Test
    public void emptyResult_onSheetWithNoEntries() throws UnsupportedFileTypeException, IOException {
        WorkbookFromFileTypeFactory factory = new WorkbookFromFileTypeFactory();
        Workbook workbook = factory.fromFile(new File(resourceHelper.getXlsxTestFilePath()));

        QueryResult result = QueryBuilder.forWorkbook(workbook)
                .fromSheet(0)
                .select(QueryColumnSelector.all())
                .build()
                .execute();

        assertThat(result.getColumns().size(), is(0));
        assertThat(result.getResultRows().size(),is(0));
    }

    @Test
    public void populatedResult_onSheetWithEntries() throws UnsupportedFileTypeException, IOException {
        WorkbookFromFileTypeFactory factory = new WorkbookFromFileTypeFactory();
        Workbook workbook = factory.fromFile(new File(resourceHelper.getXlsxTestFilePath()));

        QueryResult result = QueryBuilder.forWorkbook(workbook)
                .fromSheet(2)
                .select(QueryColumnSelector.all())
                .build()
                .execute();

        assertThat(result.getColumns().size(), is(Matchers.greaterThan(0)));
        assertThat(result.getResultRows().size(),is(Matchers.greaterThan(0)));
    }

    @Test
    public void onlySelectedColumnsAreReturned() throws UnsupportedFileTypeException, IOException {
        WorkbookFromFileTypeFactory factory = new WorkbookFromFileTypeFactory();
        Workbook workbook = factory.fromFile(new File(resourceHelper.getXlsxTestFilePath()));

        QueryResult result = QueryBuilder.forWorkbook(workbook)
                .fromSheet(2)
                .select(
                        QueryColumnSelector.column("Name")
                        .and("Job")
                )
                .build()
                .execute();

        assertThat(result.getColumns().size(), is(2));
    }
}
