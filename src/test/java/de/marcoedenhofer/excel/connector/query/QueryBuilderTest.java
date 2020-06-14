package de.marcoedenhofer.excel.connector.query;

import de.marcoedenhofer.excel.connector.utils.ResourceHelper;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.function.Supplier;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.nullValue;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertIterableEquals;

class QueryBuilderTest {
    private final ResourceHelper resourceHelper = new ResourceHelper();

    @Test
    void forWorkbook() throws IOException {
        Workbook expectedWorkbook = new XSSFWorkbook(resourceHelper.getXlsxTestFilePath());
        QueryBuilder queryBuilder = QueryBuilder.forWorkbook(expectedWorkbook);

        Workbook actualWorkbook = queryBuilder.getWorkbook();

        assertThat(actualWorkbook, is(expectedWorkbook));
    }

    @Test
    void fromFirstSheet_loadsExpectedSheet() throws IOException {
        final String expectedSheetName = "Name01";
        Workbook expectedWorkbook = new XSSFWorkbook(resourceHelper.getXlsxTestFilePath());
        QueryBuilder queryBuilder = QueryBuilder.forWorkbook(expectedWorkbook)
                .fromFirstSheet();

        String actualSheetName = queryBuilder.getSheetSupplier().get().getSheetName();

        assertThat(actualSheetName, is(expectedSheetName));
    }

    @Test
    void fromSheet_withNumber_loadsExpectedSheet() throws IOException {
        final String expectedName = "Name01";
        Workbook expectedWorkbook = new XSSFWorkbook(resourceHelper.getXlsxTestFilePath());
        QueryBuilder queryBuilder = QueryBuilder.forWorkbook(expectedWorkbook)
                .fromSheet(0);

        String actualSheetName = queryBuilder.getSheetSupplier().get().getSheetName();

        assertThat(actualSheetName, is(expectedName));
    }

    @Test
    void testFromSheet_withSheetName_loadsExpectedSheet() throws IOException {
        final String expectedName = "PersonalData";
        Workbook expectedWorkbook = new XSSFWorkbook(resourceHelper.getXlsxTestFilePath());
        QueryBuilder queryBuilder = QueryBuilder.forWorkbook(expectedWorkbook)
                .fromSheet(expectedName);

        String actualSheetName = queryBuilder.getSheetSupplier().get().getSheetName();

        assertThat(actualSheetName, is(expectedName));
    }

    @Test
    void select_buildsCorrectQueryColumnSelector() throws IOException {
        final String firstColumnName = "columnToQuery";
        final String secondColumnName = "secondColumnToQuery";
        final Workbook expectedWorkbook = new XSSFWorkbook(resourceHelper.getXlsxTestFilePath());
        final QueryColumnSelector expectedColumnSelector = QueryColumnSelector.column(firstColumnName)
                .and(secondColumnName);
        final QueryBuilder queryBuilder = QueryBuilder.forWorkbook(expectedWorkbook)
                .select(QueryColumnSelector.column(firstColumnName).and(secondColumnName));

        QueryColumnSelector actualColumnSelector = queryBuilder.getQueryColumnSelector();

        assertAll(
                () -> assertIterableEquals(expectedColumnSelector.getColumns(),actualColumnSelector.getColumns()),
                () -> assertThat(expectedColumnSelector.selectAllColumns(),is(actualColumnSelector.selectAllColumns()))
        );
    }

    @Test
    void select_withSelectAll_buildsCorrectQueryColumnSelector() throws IOException {
        final Workbook expectedWorkbook = new XSSFWorkbook(resourceHelper.getXlsxTestFilePath());
        final QueryColumnSelector expectedColumnSelector = QueryColumnSelector.all();
        final QueryBuilder queryBuilder = QueryBuilder.forWorkbook(expectedWorkbook)
                .select(QueryColumnSelector.all());

        QueryColumnSelector actualColumnSelector = queryBuilder.getQueryColumnSelector();

        assertAll(
                () -> assertIterableEquals(expectedColumnSelector.getColumns(),actualColumnSelector.getColumns()),
                () -> assertThat(expectedColumnSelector.selectAllColumns(),is(actualColumnSelector.selectAllColumns()))
        );
    }

    @Test
    void build_buildsCorrectQuery() throws IOException {
        final String firstColumnName = "columnToQuery";
        final String secondColumnName = "secondColumnToQuery";
        final String expectedSheetName = "Name01";
        final Workbook expectedWorkbook = new XSSFWorkbook(resourceHelper.getXlsxTestFilePath());
        final QueryColumnSelector expectedColumnSelector = QueryColumnSelector.column(firstColumnName)
                .and(secondColumnName);
        final QueryBuilder queryBuilder = QueryBuilder.forWorkbook(expectedWorkbook)
                .fromSheet(expectedSheetName)
                .select(QueryColumnSelector.column(firstColumnName).and(secondColumnName));
        final Query expectedQuery = new Query(queryBuilder.getSheetSupplier(), expectedColumnSelector);

        Query actualQuery = queryBuilder.build();

        QueryColumnSelector columnSelectorAfterBuild = queryBuilder.getQueryColumnSelector();
        Supplier<Sheet> sheetAfterBuild = queryBuilder.getSheetSupplier();

        assertAll(
                () -> assertThat(columnSelectorAfterBuild, is(nullValue())),
                () -> assertThat(sheetAfterBuild,is(nullValue())),
                () -> assertIterableEquals(expectedQuery.getSelectedColumns(),actualQuery.getSelectedColumns()),
                () -> assertThat(expectedQuery.getSheetSupplier(), is(actualQuery.getSheetSupplier())),
                () -> assertThat(expectedQuery.isSelectAllColumns(), is(actualQuery.isSelectAllColumns()))
        );
    }
}