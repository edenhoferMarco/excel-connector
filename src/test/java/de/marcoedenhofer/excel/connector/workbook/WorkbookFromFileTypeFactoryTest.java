package de.marcoedenhofer.excel.connector.workbook;

import de.marcoedenhofer.excel.connector.exceptions.UnsupportedFileTypeException;
import de.marcoedenhofer.excel.connector.utils.ResourceHelper;
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
    private final ResourceHelper resourceHelper = new ResourceHelper();

    @Test
    void fromFile_returnsXSSFWorkbook_forXLSXFile() throws UnsupportedFileTypeException, IOException {
        Workbook workbook = factory.fromFile(new File(resourceHelper.getXlsxTestFilePath()));

        assertThat(workbook, is(instanceOf(XSSFWorkbook.class)));
    }

    @Test
    void fromStream_returnsXSSFWorkbook_forXLSXFileStream() throws UnsupportedFileTypeException, IOException {
        final InputStream inputStream = new FileInputStream(resourceHelper.getXlsxTestFilePath());
        Workbook workbook = factory.fromStream(inputStream, resourceHelper.getXlsxTestWorksheetName());

        assertThat(workbook, is(instanceOf(XSSFWorkbook.class)));
    }

    @Test
    void fromFile_returnsHSSFWorkbook_forXLSFile() throws UnsupportedFileTypeException, IOException {
        Workbook workbook = factory.fromFile(new File(resourceHelper.getXlsTestFilePath()));

        assertThat(workbook, is(instanceOf(HSSFWorkbook.class)));
    }

    @Test
    void fromStream_returnsHSSFWorkbook_forXLSFileStream() throws UnsupportedFileTypeException, IOException {
        final InputStream inputStream = new FileInputStream(resourceHelper.getXlsTestFilePath());
        Workbook workbook = factory.fromStream(inputStream, resourceHelper.getXlsTestWorksheetName());

        assertThat(workbook, is(instanceOf(HSSFWorkbook.class)));
    }

    @Test
    void fromFile_throwsUnsupportedFileException_forODSFile() {
        assertThrows(UnsupportedFileTypeException.class, () -> factory.fromFile(new File(resourceHelper.getOdsTestFilePath())));
    }

    @Test
    void fromStream_throwsUnsupportedFileException_forODSFileStream() throws FileNotFoundException {
        final InputStream inputStream = new FileInputStream(resourceHelper.getOdsTestFilePath());

        assertThrows(UnsupportedFileTypeException.class,() -> factory.fromStream(inputStream, resourceHelper.getOdsTestWorksheetName()));
    }
}