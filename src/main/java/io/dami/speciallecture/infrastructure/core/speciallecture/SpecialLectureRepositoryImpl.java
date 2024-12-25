package io.dami.speciallecture.infrastructure.core.speciallecture;

import io.dami.speciallecture.domain.speciallecture.command.ParticipantsCommand;
import io.dami.speciallecture.domain.speciallecture.command.SpecialLecturesCommand;
import io.dami.speciallecture.domain.speciallecture.entity.SpecialLecture;
import io.dami.speciallecture.domain.speciallecture.repository.SpecialLectureRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class SpecialLectureRepositoryImpl implements SpecialLectureRepository {

    private final SpecialLectureJpaRepository specialLectureJpaRepository;

    @Override
    public List<SpecialLecture> getSpecialLecturePage(SpecialLecturesCommand command) {
        return specialLectureJpaRepository.getSpecialLecturePage(command);
    }

    @Override
    public long getSpecialLecturePageCount(SpecialLecturesCommand command) {
        return specialLectureJpaRepository.getSpecialLecturePageCount(command);
    }

    @Override
    public Optional<SpecialLecture> findById(Long specialLectureId) {
        return specialLectureJpaRepository.findById(specialLectureId);
    }
}
