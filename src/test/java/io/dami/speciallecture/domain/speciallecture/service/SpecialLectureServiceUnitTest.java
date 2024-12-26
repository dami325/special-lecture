package io.dami.speciallecture.domain.speciallecture.service;

import io.dami.speciallecture.domain.exception.CustomException;
import io.dami.speciallecture.domain.lecturer.entity.Lecturer;
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
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class SpecialLectureServiceUnitTest {

    @InjectMocks
    private SpecialLectureServiceImpl specialLectureService;

    @Mock
    private SpecialLectureRepository specialLectureRepository;
    @Mock
    private SpecialLectureParticipantRepository specialLectureParticipantRepository;
    @Mock
    private UserRepository userRepository;

    @DisplayName("신청 가능 리스트 조회 정상 성공")
    @Test
    void getAvailableSpecialLecturePageSuccess() {
        // given
        long userId = 5L;
        LocalDateTime from = LocalDateTime.of(2024, 12, 1, 0, 0);
        LocalDateTime to = LocalDateTime.of(2024, 12, 31, 0, 0);
        PageRequest pageable = PageRequest.of(0, 10);
        SpecialLecturesCommand command = new SpecialLecturesCommand(pageable, from, to, userId);
        List<SpecialLecture> specialLectures = List.of(createDefaultSpecialLecture());
        when(specialLectureRepository.getSpecialLecturePage(command)).thenReturn(specialLectures);
        when(specialLectureRepository.getSpecialLecturePageCount(command)).thenReturn(1L);

        // when
        Page<SpecialLectureInfo> result = specialLectureService.getAvailableSpecialLecturePage(command);

        // then
        assertEquals(1, result.getTotalElements());
        assertEquals(1, result.getContent().size());
        assertEquals("클린 아키텍처", result.getContent().get(0).title());

        verify(specialLectureRepository).getSpecialLecturePage(command);
        verify(specialLectureRepository).getSpecialLecturePageCount(command);
    }


    @DisplayName("특강 신청 30명 초과 '정원이 초과되었습니다.' 에러 발생 실패")
    @Test
    void registerParticipantFail1() {
        // given
        long userId = 6L;
        long specialLectureId = 10L;

        SpecialLecture specialLecture = createDefaultSpecialLecture();
        User user = createUser(userId, "주담");

        int MAXIMUM_NUMBER_OF_PEOPLE = 30;
        for (int i = 1; i <= MAXIMUM_NUMBER_OF_PEOPLE; i++) {
            specialLecture.join(createUser(userId + i, "테스트유저" + i));
        }

        when(specialLectureRepository.findById(specialLectureId)).thenReturn(Optional.of(specialLecture));
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));

        // when
        CustomException exception = Assertions.assertThrows(
                CustomException.class,
                () -> specialLectureService.registerParticipant(specialLectureId, userId),
                "정원이 초과된 경우 CustomException이 발생해야 합니다."
        );

        // then
        assertEquals("정원이 초과되었습니다.", exception.getMessage());
    }

    @DisplayName("신청 기간이 아닌 특강 '신청 가능 시간이 아닙니다.'에러 발생 실패")
    @Test
    void registerParticipantFail2() {
        // given
        long userId = 5L;
        long specialLectureId = 10L;

        SpecialLecture expiredLecture = createExpiredSpecialLecture();
        User user = createUser(userId, "주담");

        when(specialLectureRepository.findById(specialLectureId)).thenReturn(Optional.of(expiredLecture));
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));

        // when
        CustomException exception = Assertions.assertThrows(
                CustomException.class,
                () -> specialLectureService.registerParticipant(specialLectureId, userId),
                "신청 기간이 지난 경우 CustomException이 발생해야 합니다."
        );

        // then
        assertEquals("신청 가능 시간이 아닙니다.", exception.getMessage());
    }

    @DisplayName("특강 신청 이미 신청한 특강 '이미 참여한 특강입니다' 에러 발생 실패")
    @Test
    void registerParticipantFail3() {
        // given
        long userId = 5L;
        long specialLectureId = 10L;

        SpecialLecture specialLecture = createDefaultSpecialLecture();
        User user = createUser(userId, "주담");

        specialLecture.join(user); // 이미 신청된 사용자

        when(specialLectureRepository.findById(specialLectureId)).thenReturn(Optional.of(specialLecture));
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));

        // when
        CustomException exception = Assertions.assertThrows(
                CustomException.class,
                () -> specialLectureService.registerParticipant(specialLectureId, userId),
                "이미 신청한 특강의 경우 CustomException이 발생해야 합니다."
        );

        // then
        assertEquals("이미 참여한 특강 입니다.", exception.getMessage());
    }

    @DisplayName("특강 신청 완료 조회 유효하지 않은 사용자 실패")
    @Test
    void getCompletedSpecialLecturesFail1() {
        // given
        long userId = 99L;
        PageRequest pageable = PageRequest.of(0, 10);
        ParticipantsCommand command = new ParticipantsCommand(pageable,userId);
        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        // when
        CustomException exception = Assertions.assertThrows(
                CustomException.class,
                () -> specialLectureService.getSpecialLectureParticipantInfoPage(command),
                "유효하지 않은 사용자의 경우 CustomException이 발생해야 합니다."
        );

        // then
        assertEquals("유효하지 않은 사용자", exception.getMessage());
    }

    @DisplayName("특강 신청 완료 조회 성공")
    @Test
    void getCompletedSpecialLecturesSuccess() {
        // given
        long userId = 5L;
        PageRequest pageable = PageRequest.of(0, 10);
        ParticipantsCommand command = new ParticipantsCommand(pageable,userId);

        SpecialLecture specialLecture = createDefaultSpecialLecture();
        User user = createUser(userId, "주담");

        SpecialLectureParticipant specialLectureParticipant = specialLecture.join(user);// 이미 신청된 사용자

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(specialLectureParticipantRepository.getParticipantPage(command)).thenReturn(List.of(specialLectureParticipant));
        when(specialLectureParticipantRepository.getParticipantPageCount(command)).thenReturn(1L);

        // when
        Page<SpecialLectureParticipantInfo> result = specialLectureService.getSpecialLectureParticipantInfoPage(command);

        // then
        assertEquals(1, result.getTotalElements());
        assertEquals(1, result.getContent().size());
        assertEquals("클린 아키텍처", result.getContent().get(0).lectureTitle());
    }

    public static User createUser(Long id, String name) {
        return new User(id, name);
    }

    public static Lecturer createLecturer(Long userId, String userName, String introduction, String organization) {
        return new Lecturer(createUser(userId, userName), introduction, organization);
    }

    public static SpecialLecture createSpecialLecture(Lecturer lecturer, String title, LocalDateTime startTime,
                                                      LocalDateTime endTime, LocalDateTime participationStartTime,
                                                      LocalDateTime participationEndTime) {
        return lecturer.createSpecialLecture(
                title,
                startTime,
                endTime,
                participationStartTime,
                participationEndTime
        );
    }

    public static SpecialLecture createDefaultSpecialLecture() {
        Lecturer lecturer = createLecturer(10L, "주닮", "항해백엔드7기", "항해");
        return createSpecialLecture(
                lecturer,
                "클린 아키텍처",
                LocalDateTime.now().plusDays(10),
                LocalDateTime.now().plusDays(10).plusHours(3),
                LocalDateTime.now().minusDays(10),
                LocalDateTime.now().plusDays(10)
        );
    }

    public static SpecialLecture createExpiredSpecialLecture() {
        Lecturer lecturer = createLecturer(9L, "주닮강사", "항해백엔드7기", "항해");
        return createSpecialLecture(
                lecturer,
                "클린 아키텍처",
                LocalDateTime.of(2024, 12, 31, 10, 0),
                LocalDateTime.of(2024, 12, 31, 12, 0),
                LocalDateTime.of(2024, 12, 1, 0, 0),
                LocalDateTime.of(2024, 12, 25, 23, 59)
        );
    }

}
