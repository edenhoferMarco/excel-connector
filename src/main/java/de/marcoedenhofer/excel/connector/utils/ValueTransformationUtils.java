package de.marcoedenhofer.excel.connector.utils;

public class ValueTransformationUtils {
    private ValueTransformationUtils() {
        // to hide the NoArgsConstructor, it is not needed for a class with only static methods
    }

    public static Integer transformToInteger(String input) {
        return Double.valueOf(input.replace(',','.')).intValue();
    }
}
