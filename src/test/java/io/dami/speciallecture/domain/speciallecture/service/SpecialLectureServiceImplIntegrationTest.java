package io.dami.speciallecture.domain.speciallecture.service;

import io.dami.speciallecture.TestcontainersConfiguration;
import io.dami.speciallecture.domain.exception.CustomException;
import io.dami.speciallecture.domain.lecturer.entity.Lecturer;
import io.dami.speciallecture.domain.speciallecture.command.SpecialLecturesCommand;
import io.dami.speciallecture.domain.speciallecture.entity.SpecialLecture;
import io.dami.speciallecture.domain.speciallecture.result.SpecialLectureInfo;
import io.dami.speciallecture.domain.user.entity.User;
import io.dami.speciallecture.infrastructure.core.lecturer.LecturerJpaRepository;
import io.dami.speciallecture.infrastructure.core.speciallecture.SpecialLectureJpaRepository;
import io.dami.speciallecture.infrastructure.core.speciallecture.participant.SpecialLectureParticipantJpaRepository;
import io.dami.speciallecture.infrastructure.core.user.UserJpaRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Import(TestcontainersConfiguration.class)
@SpringBootTest
class SpecialLectureServiceImplIntegrationTest {

    @Autowired
    private SpecialLectureService specialLectureService;

    @Autowired
    private UserJpaRepository userJpaRepository;

    @Autowired
    private LecturerJpaRepository lecturerJpaRepository;

    @Autowired
    private SpecialLectureJpaRepository specialLectureJpaRepository;

    @Autowired
    private SpecialLectureParticipantJpaRepository specialLectureParticipantJpaRepository;

    private Long userId;
    private Lecturer lecturer;
    private SpecialLecture specialLecture;
    private final int MAXIMUM_NUMBER_OF_PEOPLE = 30;

    @BeforeEach
    void init() {
        // Create and save user
        User user = new User(null, "주닮");
        this.userId = userJpaRepository.save(user).getId();

        // Create and save lecturer
        User user2 = userJpaRepository.save(new User(null, "항해"));
        lecturer = new Lecturer(user2, "항해백엔드7기", "항해");

        // Create and save a default special lecture
        SpecialLecture defaultSpecialLecture = lecturer.createSpecialLecture(
                "클린 아키텍처",
                LocalDateTime.now().plusDays(10),
                LocalDateTime.now().plusDays(10).plusHours(3),
                LocalDateTime.now().minusDays(10),
                LocalDateTime.now().plusDays(10)
        );
        lecturerJpaRepository.save(lecturer);
        this.specialLecture = defaultSpecialLecture;
    }

    @AfterEach
    void tearDown() {
        userJpaRepository.deleteAll();
        lecturerJpaRepository.deleteAll();
        specialLectureJpaRepository.deleteAll();
        specialLectureParticipantJpaRepository.deleteAll();
    }

    @DisplayName("신청 가능 특강 목록 조회 성공")
    @Test
    void getAvailableSpecialLecturePage() {
        //given
        Pageable pageable = PageRequest.of(0, 10);
        LocalDateTime from = LocalDateTime.now();
        LocalDateTime to = LocalDateTime.now().plusDays(1l);
        SpecialLecturesCommand command = new SpecialLecturesCommand(pageable, from, to, userId);

        //when
        Page<SpecialLectureInfo> result = specialLectureService.getAvailableSpecialLecturePage(command);

        //then
        List<SpecialLectureInfo> content = result.getContent();
        Assertions.assertThat(content.size()).isEqualTo(1l);
        Assertions.assertThat(content.get(0).title()).isEqualTo("클린 아키텍처");
        Assertions.assertThat(content.get(0).isParticipationOpen()).isTrue();
    }

    @DisplayName("선착순 30명 이후의 신청자의 경우 정원초과 에러 발생 실패")
    @Test
    void registerParticipantFail1() {
        //given
        for (int i = 1; i <= MAXIMUM_NUMBER_OF_PEOPLE; i++) {
            User user = userJpaRepository.save(new User(null, "테스트" + i));
            specialLecture.join(user);
        }
        specialLectureJpaRepository.save(specialLecture);

        //when && then
        Assertions.assertThatThrownBy(() -> specialLectureService.registerParticipant(specialLecture.getId(), userId))
                .isInstanceOf(CustomException.class)
                .hasMessage("정원이 초과되었습니다.");
    }

    @DisplayName("동시에 동일한 특강에 대해 40명이 신청했을 때, 30명만 성공")
    @Test
    void registerParticipantSuccess() throws InterruptedException {
        //given
        final int USERS = 40;
        ExecutorService executorService = Executors.newFixedThreadPool(USERS);
        CountDownLatch latch = new CountDownLatch(USERS);

        //when
        for (int i = 1; i <= USERS; i++) {
            final int index = i;
            executorService.submit(() -> {
                try {
                    User user = userJpaRepository.save(new User(null, "테스트" + index));
                    specialLectureService.registerParticipant(specialLecture.getId(), user.getId());
                } catch (CustomException e) {
                    System.out.println("index = " + index + "fail");
                } finally {
                    latch.countDown();
                }
            });
        }
        latch.await();

        // then
        SpecialLecture specialLecture = specialLectureJpaRepository.findFetchById(this.specialLecture.getId()).get();
        Assertions.assertThat(specialLecture.getSpecialLectureParticipants().size()).isEqualTo(30);
        Assertions.assertThat(specialLecture.getCurrentCount()).isEqualTo(30);
    }


}
