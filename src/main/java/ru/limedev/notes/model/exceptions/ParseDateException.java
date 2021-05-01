package ru.limedev.notes.model.exceptions;

import androidx.annotation.NonNull;

public class ParseDateException extends Exception {

    private String reason;

    public ParseDateException() {
        super();
    }

    public ParseDateException(Exception exception, String reason) {
        super(exception);
        this.reason = reason;
    }

    public ParseDateException(String cause, String reason) {
        super(cause);
        this.reason = reason;
    }

    public ParseDateException(String reason) {
        super();
        this.reason = reason;
    }

    @NonNull
    public String toString() {
        String message = getMessage();
        if (message != null && reason != null) {
            return getMessage() + ": " + reason;
        }
        return "Error during parse date!";
    }
}
