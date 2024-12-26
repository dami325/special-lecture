package io.dami.speciallecture.domain.lecturer.entity;

import io.dami.speciallecture.domain.speciallecture.entity.SpecialLecture;
import io.dami.speciallecture.domain.user.entity.User;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Comment;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static jakarta.persistence.ConstraintMode.NO_CONSTRAINT;

@Getter
@Entity
@Comment("강연자")
@Table(name = "tb_lecturer")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Lecturer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false, unique = true, foreignKey = @ForeignKey(NO_CONSTRAINT))
    @Comment("사용자")
    private User user;

    @Comment("소개")
    private String introduction;

    @Comment("소속")
    private String organization;

    @OneToMany(mappedBy = "lecturer", cascade = {CascadeType.PERSIST, CascadeType.MERGE}, orphanRemoval = true)
    private List<SpecialLecture> specialLectures = new ArrayList<>();

    /**
     * 특강 생성
     */
    public SpecialLecture createSpecialLecture(String title, LocalDateTime specialLectureStartTime,
                                               LocalDateTime specialLectureEndTime, LocalDateTime participationStartTime,
                                               LocalDateTime participationEndTime) {

        SpecialLecture newSpecialLecture = SpecialLecture.builder()
                .title(title)
                .specialLectureStartTime(specialLectureStartTime)
                .specialLectureEndTime(specialLectureEndTime)
                .participationStartTime(participationStartTime)
                .participationEndTime(participationEndTime)
                .lecturer(this)
                .build();

        this.specialLectures.add(newSpecialLecture);

        return newSpecialLecture;
    }

    public Lecturer(User user, String introduction, String organization) {
        this.user = user;
        this.introduction = introduction;
        this.organization = organization;
        this.specialLectures = new ArrayList<>();
    }
}
