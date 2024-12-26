package io.dami.speciallecture.domain.speciallecture.result;

import io.dami.speciallecture.domain.speciallecture.entity.SpecialLecture;

import java.time.LocalDateTime;

public record SpecialLectureInfo(
        Long specialLectureId,
        boolean isParticipationOpen,
        String title,
        String lecturerName,
        int currentCount,
        int capacity,
        LocalDateTime specialLectureStartTime,
        LocalDateTime specialLectureEndTime,
        LocalDateTime participationStartTime,
        LocalDateTime participationEndTime
) {
    public static SpecialLectureInfo mapToInfo(SpecialLecture specialLecture) {
        return new SpecialLectureInfo(
                specialLecture.getId(),
                specialLecture.isParticipationOpen() && !specialLecture.isFull(),
                specialLecture.getTitle(),
                specialLecture.getLecturer().getUser().getUsername(),
                specialLecture.getCurrentCount(),
                specialLecture.getCapacity(),
                specialLecture.getSpecialLectureStartTime(),
                specialLecture.getSpecialLectureEndTime(),
                specialLecture.getParticipationStartTime(),
                specialLecture.getParticipationEndTime()
        );
    }
}
