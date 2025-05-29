package it.epicode.Capstone_ReGive_back_end.auth;

import java.util.Map;

public class MultipleFieldsConflictException extends RuntimeException {
    private final Map<String, String> errors;

    public MultipleFieldsConflictException(Map<String, String> errors) {
        super("Errore nella validazione dei campi");
        this.errors = errors;
    }

    public Map<String, String> getErrors() {
        return errors;
    }
}