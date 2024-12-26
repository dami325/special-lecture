package io.dami.speciallecture.infrastructure.core.speciallecture;

import io.dami.speciallecture.domain.speciallecture.command.SpecialLecturesCommand;
import io.dami.speciallecture.domain.speciallecture.entity.SpecialLecture;

import java.util.List;
import java.util.Optional;

public interface SpecialLectureJpaRepositoryCustom {
    List<SpecialLecture> getSpecialLecturePage(SpecialLecturesCommand command);

    long getSpecialLecturePageCount(SpecialLecturesCommand command);

    Optional<SpecialLecture> selectForUpdateById(Long specialLectureId);

    Optional<SpecialLecture> findFetchById(Long id);
}
