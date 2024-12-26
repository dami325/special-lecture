package io.dami.speciallecture.interfaces.request;

import io.dami.speciallecture.domain.speciallecture.command.ParticipantsCommand;
import org.springframework.data.domain.PageRequest;

public record ParticipantsRequest(
        int page,
        int size,
        Long userId
) {
    public ParticipantsRequest {
        if (page < 0) {
            throw new IllegalArgumentException("Page must be non-negative.");
        }
        if (size <= 0) {
            throw new IllegalArgumentException("Size must be greater than zero.");
        }
        if (userId == null || userId <= 0) {
            throw new IllegalArgumentException("User ID must be positive and non-null.");
        }
    }

    public ParticipantsCommand toCommand() {
        return new ParticipantsCommand(
                PageRequest.of(page, size),
                userId
        );
    }

}
