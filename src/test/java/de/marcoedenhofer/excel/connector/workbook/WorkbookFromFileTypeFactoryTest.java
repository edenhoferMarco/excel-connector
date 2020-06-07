package de.marcoedenhofer.excel.connector.workbook;

import de.marcoedenhofer.excel.connector.exceptions.UnsupportedFileTypeException;
import de.marcoedenhofer.excel.connector.utils.TestResourceHelper;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.junit.jupiter.api.Test;

import java.io.*;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertThrows;

class WorkbookFromFileTypeFactoryTest {
    private final WorkbookFromFileTypeFactory factory = new WorkbookFromFileTypeFactory();
    private final TestResourceHelper testResourceHelper = new TestResourceHelper();

    @Test
    void fromFile_returnsXSSFWorkbook_forXLSXFile() throws UnsupportedFileTypeException, IOException {
        Workbook workbook = factory.fromFile(new File(testResourceHelper.getXlsxTestFilePath()));

        assertThat(workbook, is(instanceOf(XSSFWorkbook.class)));
    }

    @Test
    void fromStream_returnsXSSFWorkbook_forXLSXFileStream() throws UnsupportedFileTypeException, IOException {
        final InputStream inputStream = new FileInputStream(testResourceHelper.getXlsxTestFilePath());
        Workbook workbook = factory.fromStream(inputStream, testResourceHelper.getXlsxTestWorksheetName());

        assertThat(workbook, is(instanceOf(XSSFWorkbook.class)));
    }

    @Test
    void fromFile_returnsHSSFWorkbook_forXLSFile() throws UnsupportedFileTypeException, IOException {
        Workbook workbook = factory.fromFile(new File(testResourceHelper.getXlsTestFilePath()));

        assertThat(workbook, is(instanceOf(HSSFWorkbook.class)));
    }

    @Test
    void fromStream_returnsHSSFWorkbook_forXLSFileStream() throws UnsupportedFileTypeException, IOException {
        final InputStream inputStream = new FileInputStream(testResourceHelper.getXlsTestFilePath());
        Workbook workbook = factory.fromStream(inputStream, testResourceHelper.getXlsTestWorksheetName());

        assertThat(workbook, is(instanceOf(HSSFWorkbook.class)));
    }

    @Test
    void fromFile_throwsUnsupportedFileException_forODSFile() {
        assertThrows(UnsupportedFileTypeException.class, () -> factory.fromFile(new File(testResourceHelper.getOdsTestFilePath())));
    }

    @Test
    void fromStream_throwsUnsupportedFileException_forODSFileStream() throws FileNotFoundException {
        final InputStream inputStream = new FileInputStream(testResourceHelper.getOdsTestFilePath());

        assertThrows(UnsupportedFileTypeException.class,() -> factory.fromStream(inputStream, testResourceHelper.getOdsTestWorksheetName()));
    }
}