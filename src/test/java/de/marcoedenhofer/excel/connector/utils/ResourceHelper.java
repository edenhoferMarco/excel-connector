package de.marcoedenhofer.excel.connector.utils;

import lombok.Getter;

@Getter
public class ResourceHelper {
    private final String testSrcPath = "src/test/resources";
    private final String xlsxTestWorksheetName = "testWorksheet.xlsx";
    private final String xlsTestWorksheetName = "testWorksheet.xls";
    private final String odsTestWorksheetName = "testWorksheet.ods";
    private final String xlsTestFilePath = testSrcPath + "/" + xlsTestWorksheetName;
    private final String xlsxTestFilePath = testSrcPath + "/" + xlsxTestWorksheetName;
    private final String odsTestFilePath = testSrcPath + "/" + odsTestWorksheetName;
}
