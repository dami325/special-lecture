package io.dami.speciallecture.domain.speciallecture.repository;

import io.dami.speciallecture.domain.speciallecture.command.SpecialLecturesCommand;
import io.dami.speciallecture.domain.speciallecture.entity.SpecialLecture;

import java.util.List;
import java.util.Optional;

public interface SpecialLectureRepository {

    List<SpecialLecture> getSpecialLecturePage(SpecialLecturesCommand command);

    long getSpecialLecturePageCount(SpecialLecturesCommand command);

    Optional<SpecialLecture> findById(Long specialLectureId);

    Optional<SpecialLecture> selectForUpdateById(Long specialLectureId);
}
