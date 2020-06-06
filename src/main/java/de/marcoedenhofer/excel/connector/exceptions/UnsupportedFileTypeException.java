package de.marcoedenhofer.excel.connector.exceptions;

public class UnsupportedFileTypeException extends Exception {
    public UnsupportedFileTypeException(String message) {
        super(message);
    }

    public UnsupportedFileTypeException(Throwable cause) {
        super(cause);
    }

    public UnsupportedFileTypeException(String message, Throwable cause) {
        super(message, cause);
    }

}
