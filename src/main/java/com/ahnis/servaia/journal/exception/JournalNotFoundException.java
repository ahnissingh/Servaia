package com.ahnis.servaia.journal.exception;

public class JournalNotFoundException extends RuntimeException {
    public JournalNotFoundException(String string) {
        super(string);
    }
}
