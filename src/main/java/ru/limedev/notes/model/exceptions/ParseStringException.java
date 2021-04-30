package ru.limedev.notes.model.exceptions;

import androidx.annotation.NonNull;

public class ParseStringException extends Exception {

    private String reason;

    public ParseStringException() {
        super();
    }

    public ParseStringException(Exception exception, String reason) {
        super(exception);
        this.reason = reason;
    }

    public ParseStringException(String cause, String reason) {
        super(cause);
        this.reason = reason;
    }

    public ParseStringException(String reason) {
        super();
        this.reason = reason;
    }

    @NonNull
    public String toString() {
        String message = getMessage();
        if (message != null && reason != null) {
            return getMessage() + ": " + reason;
        }
        return "Error during parse value!";
    }
}
