package io.dami.speciallecture.domain.speciallecture.entity;

import io.dami.speciallecture.domain.exception.CustomException;
import io.dami.speciallecture.domain.lecturer.entity.Lecturer;
import io.dami.speciallecture.domain.user.entity.User;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.Comment;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Builder
@Getter
@Entity
@Comment(value = "특강")
@Table(name = "tb_special_lecture")
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class SpecialLecture {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Comment("특강 이름")
    private String title;

    @Comment("강연자")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "lecturer_id")
    private Lecturer lecturer;

    @Comment("현재 인원")
    @Builder.Default
    private int currentCount = 0;

    @Comment("정원")
    @Builder.Default
    private int capacity = 30;

    @Comment("특강 시작 시간")
    private LocalDateTime specialLectureStartTime;

    @Comment("특강 종료 시간")
    private LocalDateTime specialLectureEndTime;

    @Comment("특강 신청 시작 시간")
    private LocalDateTime participationStartTime;

    @Comment("특강 신청 종료 시간")
    private LocalDateTime participationEndTime;

    @Builder.Default
    @OneToMany(mappedBy = "specialLecture", cascade = {CascadeType.PERSIST,CascadeType.MERGE}, orphanRemoval = true)
    private Set<SpecialLectureParticipant> specialLectureParticipants = new HashSet<>();


    /**
     * 특강 신청 가능 여부 확인
     */
    public boolean isParticipationOpen() {
        LocalDateTime now = LocalDateTime.now();
        return now.isAfter(participationStartTime) && now.isBefore(participationEndTime);
    }

    /**
     * 정원 초과 확인
     */
    public boolean isFull(){
        return currentCount >= capacity;
    }

    public SpecialLectureParticipant join(User user) {
        if (!isParticipationOpen()) {
            throw new CustomException("신청 가능 시간이 아닙니다.");
        }
        if (isUserContainsParticipant(user)) {
            throw new CustomException("이미 참여한 특강 입니다.");
        }
        if (isFull()) {
            throw new CustomException("정원이 초과되었습니다.");
        }
        SpecialLectureParticipant specialLectureParticipant = new SpecialLectureParticipant(user, this);
        specialLectureParticipants.add(specialLectureParticipant);

        this.currentCount++;

        return specialLectureParticipant;
    }

    /**
     * 이미 참여 한 사용자인지
     */
    public boolean isUserContainsParticipant(User user) {
        return specialLectureParticipants.stream()
                .anyMatch(specialLectureParticipant -> specialLectureParticipant.getUser().getId().equals(user.getId()));
    }
}
