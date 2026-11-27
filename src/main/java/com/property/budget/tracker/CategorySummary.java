package com.property.budget.tracker;

public record CategorySummary(
        Long id,
        String name,
        String type,
        boolean active
) {}
