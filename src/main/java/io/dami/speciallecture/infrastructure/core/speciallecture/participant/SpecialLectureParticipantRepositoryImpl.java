package io.dami.speciallecture.infrastructure.core.speciallecture.participant;

import io.dami.speciallecture.domain.speciallecture.command.ParticipantsCommand;
import io.dami.speciallecture.domain.speciallecture.entity.SpecialLectureParticipant;
import io.dami.speciallecture.domain.speciallecture.repository.SpecialLectureParticipantRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class SpecialLectureParticipantRepositoryImpl implements SpecialLectureParticipantRepository {

    private final SpecialLectureParticipantJpaRepository specialLectureParticipantJpaRepository;

    @Override
    public List<SpecialLectureParticipant> getParticipantPage(ParticipantsCommand command) {
        return specialLectureParticipantJpaRepository.getParticipantPage(command);
    }

    @Override
    public long getParticipantPageCount(ParticipantsCommand command) {
        return specialLectureParticipantJpaRepository.getParticipantPageCount(command);
    }
}
