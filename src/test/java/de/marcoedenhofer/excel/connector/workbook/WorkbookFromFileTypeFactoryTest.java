package de.marcoedenhofer.excel.connector.workbook;

import de.marcoedenhofer.excel.connector.exceptions.UnsupportedFileTypeException;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertThrows;

class WorkbookFromFileTypeFactoryTest {
    private final WorkbookFromFileTypeFactory factory = new WorkbookFromFileTypeFactory();

    private final String testSrcPath = "src/test/resources";
    private final String xlsxTestWorksheet = "testWorksheet.xlsx";
    private final String xlsTestWorksheet = "testWorksheet.xls";
    private final String odsTestWorksheet = "testWorksheet.ods";
    private final String xlsTestFilePath = testSrcPath + "/" + xlsTestWorksheet;
    private final String xlsxTestFilePath = testSrcPath + "/" + xlsxTestWorksheet;
    private final String odsTestFilePath = testSrcPath + "/" + odsTestWorksheet;

    @Test
    void fromFile_returnsXSSFWorkbook_forXLSXFile() throws UnsupportedFileTypeException, IOException {
        Workbook workbook = factory.fromFile(new File(xlsxTestFilePath));

        assertThat(workbook, is(instanceOf(XSSFWorkbook.class)));
    }

    @Test
    void fromStream_returnsXSSFWorkbook_forXLSXFileStream() throws UnsupportedFileTypeException, IOException {
        final InputStream inputStream = new FileInputStream(xlsxTestFilePath);
        Workbook workbook = factory.fromStream(inputStream, xlsxTestWorksheet);

        assertThat(workbook, is(instanceOf(XSSFWorkbook.class)));
    }

    @Test
    void fromFile_returnsHSSFWorkbook_forXLSFile() throws UnsupportedFileTypeException, IOException {
        Workbook workbook = factory.fromFile(new File(xlsTestFilePath));

        assertThat(workbook, is(instanceOf(HSSFWorkbook.class)));
    }

    @Test
    void fromStream_returnsHSSFWorkbook_forXLSFileStream() throws UnsupportedFileTypeException, IOException {
        final InputStream inputStream = new FileInputStream(xlsTestFilePath);
        Workbook workbook = factory.fromStream(inputStream, xlsTestWorksheet);

        assertThat(workbook, is(instanceOf(HSSFWorkbook.class)));
    }

    @Test
    void fromFile_throwsUnsupportedFileException_forODSFile() throws IOException {
        assertThrows(UnsupportedFileTypeException.class, () -> factory.fromFile(new File(odsTestFilePath)));
    }

    @Test
    void fromStream_throwsUnsupportedFileException_forODSFileStream() throws IOException {
        final InputStream inputStream = new FileInputStream(odsTestFilePath);

        assertThrows(UnsupportedFileTypeException.class,() -> factory.fromStream(inputStream, odsTestWorksheet));
    }
}