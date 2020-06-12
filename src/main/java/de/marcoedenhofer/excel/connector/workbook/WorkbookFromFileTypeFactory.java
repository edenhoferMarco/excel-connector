package de.marcoedenhofer.excel.connector.workbook;

import de.marcoedenhofer.excel.connector.exceptions.UnsupportedFileTypeException;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

public class WorkbookFromFileTypeFactory implements IWorkbookFactory {
    @Override
    public Workbook fromFile(File file) throws UnsupportedFileTypeException, IOException {
        final FileTypes fileType = getFileType(file.getName());

        return getWorkbook(new FileInputStream(file), fileType);
    }

    @Override
    public Workbook fromStream(InputStream stream, String fileName) throws UnsupportedFileTypeException, IOException {
        final FileTypes fileType = getFileType(fileName);

        return getWorkbook(stream, fileType);
    }

    private Workbook getWorkbook(InputStream inputStream, FileTypes fileType) throws IOException, UnsupportedFileTypeException {
        switch (fileType) {
            case XLS: return new HSSFWorkbook(inputStream);
            case XLSX: return new XSSFWorkbook(inputStream);
            default: throw new UnsupportedFileTypeException("File with type ." + fileType + " is not supported");
        }
    }

    private FileTypes getFileType(String fileName) throws UnsupportedFileTypeException {
        final String fileEnding = fileName
                .substring(fileName.lastIndexOf('.') + 1)
                .strip()
                .toLowerCase();

        switch (fileEnding) {
            case "xls": return FileTypes.XLS;
            case "xlsx": return FileTypes.XLSX;
            default: throw new UnsupportedFileTypeException("File with type ." + fileEnding + " is not supported");
        }
    }
}
