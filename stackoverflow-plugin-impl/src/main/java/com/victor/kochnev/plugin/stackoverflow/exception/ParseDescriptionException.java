package com.victor.kochnev.plugin.stackoverflow.exception;

public class ParseDescriptionException extends ModuleException {
    public ParseDescriptionException(String message, Throwable cause) {
        super(message, cause);
    }

    public ParseDescriptionException(String message) {
        super(message);
    }
}
