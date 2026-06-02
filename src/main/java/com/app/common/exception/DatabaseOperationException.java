package com.app.common.exception;

public class DatabaseOperationException extends RuntimeException {
    // Constructor nhận thông báo lỗi
    public DatabaseOperationException(String message) {
        super(message);
    }

    // Constructor nhận thông báo lỗi và lỗi gốc (để giữ lại dấu vết stack trace)
    public DatabaseOperationException(String message, Throwable cause) {
        super(message, cause);
    }
}
