package io.dami.speciallecture.domain.speciallecture.result;

import io.dami.speciallecture.domain.lecturer.entity.Lecturer;
import io.dami.speciallecture.domain.speciallecture.entity.SpecialLecture;
import io.dami.speciallecture.domain.speciallecture.entity.SpecialLectureParticipant;
import io.dami.speciallecture.domain.user.entity.User;

import java.time.LocalDateTime;

public record SpecialLectureParticipantInfo(
        Long specialLectureParticipantId,
        Long specialLectureId,
        String lectureTitle,
        String lecturerName,
        LocalDateTime createdDate
) {
    public static SpecialLectureParticipantInfo mapToInfo(SpecialLectureParticipant specialLectureParticipant) {
        SpecialLecture specialLecture = specialLectureParticipant.getSpecialLecture();
        Lecturer lecturer = specialLecture.getLecturer();
        User user = lecturer.getUser();
        return new SpecialLectureParticipantInfo(
                specialLectureParticipant.getId(),
                specialLecture.getId(),
                specialLecture.getTitle(),
                user.getUsername(),
                specialLectureParticipant.getCreatedDate()
        );
    }
}
