package io.dami.speciallecture.domain.speciallecture.repository;

import io.dami.speciallecture.domain.speciallecture.command.ParticipantsCommand;
import io.dami.speciallecture.domain.speciallecture.entity.SpecialLectureParticipant;

import java.util.List;

public interface SpecialLectureParticipantRepository {

    List<SpecialLectureParticipant> getParticipantPage(ParticipantsCommand command);

    long getParticipantPageCount(ParticipantsCommand command);
}
