package io.dami.speciallecture.domain.speciallecture.service;

import io.dami.speciallecture.domain.speciallecture.command.ParticipantsCommand;
import io.dami.speciallecture.domain.speciallecture.command.SpecialLecturesCommand;
import io.dami.speciallecture.domain.speciallecture.result.SpecialLectureInfo;
import io.dami.speciallecture.domain.speciallecture.result.SpecialLectureParticipantInfo;
import org.springframework.data.domain.Page;

public interface SpecialLectureService {

    Page<SpecialLectureInfo> getAvailableSpecialLecturePage(SpecialLecturesCommand command);

    SpecialLectureParticipantInfo registerParticipant(Long specialLectureId, Long userId);

    Page<SpecialLectureParticipantInfo> getSpecialLectureParticipantInfoPage(ParticipantsCommand command);
}
