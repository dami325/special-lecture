package io.dami.speciallecture.infrastructure.core.speciallecture;

import io.dami.speciallecture.domain.speciallecture.command.SpecialLecturesCommand;
import io.dami.speciallecture.domain.speciallecture.entity.SpecialLecture;

import java.util.List;

public interface SpecialLectureJpaRepositoryCustom {
    List<SpecialLecture> getSpecialLecturePage(SpecialLecturesCommand command);

    long getSpecialLecturePageCount(SpecialLecturesCommand command);
}
