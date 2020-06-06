package de.marcoedenhofer.excel.connector.workbook;

import de.marcoedenhofer.excel.connector.exceptions.UnsupportedFileTypeException;
import org.apache.poi.ss.usermodel.Workbook;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

public interface IWorkbookFactory {
    /**
     * Returns the appropriate {@link org.apache.poi.ss.usermodel.Workbook} for the provided File.
     * @param file the .xls or .xlsx file to get the {@link org.apache.poi.ss.usermodel.Workbook} from.
     * @return the fitting {@link org.apache.poi.ss.usermodel.Workbook}
     * @throws UnsupportedFileTypeException if a different file type as .xls or .xlsx is provided.
     * @throws IOException if the specified file can not be found or opened.
     */
    Workbook fromFile(File file) throws UnsupportedFileTypeException, IOException;
    /**
     * Returns the appropriate {@link org.apache.poi.ss.usermodel.Workbook} for the provided stream and fileName.
     * @param stream the stream of the .xls or .xlsx file to get the {@link org.apache.poi.ss.usermodel.Workbook} from.
     * @param fileName the name of the file. This is needed to extract the file type.
     * @return the fitting {@link org.apache.poi.ss.usermodel.Workbook}
     * @throws UnsupportedFileTypeException if a different file type as .xls or .xlsx is provided.
     * @throws IOException if the specified file can not be found or opened.
     */
    Workbook fromStream(InputStream stream, String fileName) throws UnsupportedFileTypeException, IOException;
}
