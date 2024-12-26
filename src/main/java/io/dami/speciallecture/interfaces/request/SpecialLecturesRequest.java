package io.dami.speciallecture.interfaces.request;

import io.dami.speciallecture.domain.speciallecture.command.SpecialLecturesCommand;
import org.springframework.data.domain.PageRequest;

import java.time.LocalDate;

public record SpecialLecturesRequest(
        int page,
        int size,
        LocalDate date,
        Long userId
) {
    public SpecialLecturesRequest {
        if (page < 0) {
            throw new IllegalArgumentException("Page must be non-negative.");
        }
        if (size <= 0) {
            throw new IllegalArgumentException("Size must be greater than zero.");
        }
        if (date == null) {
            throw new IllegalArgumentException("Date must not be null.");
        }
        if (userId == null || userId <= 0) {
            throw new IllegalArgumentException("User ID must be positive and non-null.");
        }
    }

    public SpecialLecturesCommand toCommand() {
        return new SpecialLecturesCommand(
                PageRequest.of(page, size),
                date.atStartOfDay(),
                date.atTime(23, 59, 59),
                userId
        );
    }

}
