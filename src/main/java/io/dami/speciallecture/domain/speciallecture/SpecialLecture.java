package io.dami.speciallecture.domain.speciallecture;

import io.dami.speciallecture.domain.lecturer.Lecturer;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Comment;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Getter
@Entity
@Comment(value = "특강")
@Table(name = "tb_special_lecture")
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
    private int currentCount;

    @Comment("정원")
    private int capacity = 30;

    @Comment("특강 시작 시간")
    private LocalDateTime specialLectureStartTime;

    @Comment("특강 종료 시간")
    private LocalDateTime specialLectureEndTime;

    @Comment("특강 신청 시작 시간")
    private LocalDateTime participationStartTime;

    @Comment("특강 신청 종료 시간")
    private LocalDateTime participationEndTime;

    @OneToMany(mappedBy = "specialLecture", cascade = {CascadeType.PERSIST,CascadeType.MERGE}, orphanRemoval = true)
    private Set<SpecialLectureParticipant> specialLectureParticipants = new HashSet<>();

    /**
     * 특강 신청 가능 여부 확인
     */
    public boolean isParticipationOpen() {
        LocalDateTime now = LocalDateTime.now();
        return now.isAfter(participationStartTime) && now.isBefore(participationEndTime) && currentCount < capacity;
    }
}
