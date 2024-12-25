package io.dami.speciallecture.domain.speciallecture.command;

import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;

public record ParticipantsCommand(
        Pageable pageable,
        Long userId
)
{
    public ParticipantsCommand {
        if (pageable == null) {
            throw new IllegalArgumentException("Pageable must not be null.");
        }
        if (userId == null || userId <= 0) {
            throw new IllegalArgumentException("User ID must be a positive number and not null.");
        }
    }
}
