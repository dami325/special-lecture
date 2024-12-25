package io.dami.speciallecture.domain.speciallecture.command;

import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.time.LocalDateTime;

public record SpecialLecturesCommand(
        Pageable pageable,
        LocalDateTime from,
        LocalDateTime to,
        Long userId
)
{
    public SpecialLecturesCommand {
        if (pageable == null) {
            throw new IllegalArgumentException("Pageable must not be null.");
        }
        if (from == null) {
            throw new IllegalArgumentException("'from' date must not be null.");
        }
        if (to == null) {
            throw new IllegalArgumentException("'to' date must not be null.");
        }
        if (from.isAfter(to)) {
            throw new IllegalArgumentException("'from' date must be before or equal to 'to' date.");
        }
        if (userId == null || userId <= 0) {
            throw new IllegalArgumentException("User ID must be a positive number and not null.");
        }
    }
}
