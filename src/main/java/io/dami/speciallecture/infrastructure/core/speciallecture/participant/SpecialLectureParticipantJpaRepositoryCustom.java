package io.dami.speciallecture.infrastructure.core.speciallecture.participant;

import io.dami.speciallecture.domain.speciallecture.command.ParticipantsCommand;
import io.dami.speciallecture.domain.speciallecture.entity.SpecialLectureParticipant;

import java.util.List;

public interface SpecialLectureParticipantJpaRepositoryCustom {
    List<SpecialLectureParticipant> getParticipantPage(ParticipantsCommand command);

    long getParticipantPageCount(ParticipantsCommand command);
}
