package io.dami.speciallecture.domain.speciallecture.service;

import io.dami.speciallecture.domain.exception.CustomException;
import io.dami.speciallecture.domain.speciallecture.command.ParticipantsCommand;
import io.dami.speciallecture.domain.speciallecture.command.SpecialLecturesCommand;
import io.dami.speciallecture.domain.speciallecture.entity.SpecialLecture;
import io.dami.speciallecture.domain.speciallecture.entity.SpecialLectureParticipant;
import io.dami.speciallecture.domain.speciallecture.repository.SpecialLectureParticipantRepository;
import io.dami.speciallecture.domain.speciallecture.repository.SpecialLectureRepository;
import io.dami.speciallecture.domain.speciallecture.result.SpecialLectureInfo;
import io.dami.speciallecture.domain.speciallecture.result.SpecialLectureParticipantInfo;
import io.dami.speciallecture.domain.user.entity.User;
import io.dami.speciallecture.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Component
@RequiredArgsConstructor
public class SpecialLectureServiceImpl implements SpecialLectureService {

    private final SpecialLectureRepository specialLectureRepository;
    private final SpecialLectureParticipantRepository specialLectureParticipantRepository;
    private final UserRepository userRepository;

    @Transactional(readOnly = true)
    @Override
    public Page<SpecialLectureInfo> getAvailableSpecialLecturePage(SpecialLecturesCommand command) {
        List<SpecialLectureInfo> content = specialLectureRepository.getSpecialLecturePage(command).stream()
                .map(SpecialLectureInfo::mapToInfo)
                .toList();

        long total = specialLectureRepository.getSpecialLecturePageCount(command);

        return new PageImpl<>(content, command.pageable(), total);
    }

    /**
     * 특강 신청
     * - 특정 userId 로 선착순으로 제공되는 특강을 신청하는 API 를 작성합니다.
     * - 동일한 신청자는 동일한 강의에 대해서 한 번의 수강 신청만 성공할 수 있습니다.
     * - 특강은 선착순 30명만 신청 가능합니다.
     * - 이미 신청자가 30명이 초과 되면 이후 신청자는 요청을 실패합니다.
     */
    @Transactional
    @Override
    public SpecialLectureParticipantInfo registerParticipant(Long specialLectureId, Long userId) {

        SpecialLecture specialLecture = specialLectureRepository.findById(specialLectureId)
                .orElseThrow(() -> new CustomException("유효하지 않은 강의"));

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new CustomException("유효하지 않은 사용자"));

        SpecialLectureParticipant specialLectureParticipant = specialLecture.join(user);

        return SpecialLectureParticipantInfo.mapToInfo(specialLectureParticipant);
    }

    @Transactional(readOnly = true)
    @Override
    public Page<SpecialLectureParticipantInfo> getSpecialLectureParticipantInfoPage(ParticipantsCommand command) {
        Long userId = command.userId();

        userRepository.findById(userId)
                .orElseThrow(() -> new CustomException("유효하지 않은 사용자"));

        List<SpecialLectureParticipantInfo> content = specialLectureParticipantRepository.getParticipantPage(command).stream()
                .map(SpecialLectureParticipantInfo::mapToInfo)
                .toList();

        long total = specialLectureParticipantRepository.getParticipantPageCount(command);

        return new PageImpl<>(content, command.pageable(), total);
    }
}
