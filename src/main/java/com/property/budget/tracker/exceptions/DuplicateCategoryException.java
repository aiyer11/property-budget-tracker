package com.property.budget.tracker.exceptions;

public class DuplicateCategoryException extends RuntimeException {
    public DuplicateCategoryException(String name) {
        super("Category already exists with name: " + name);
    }
}
