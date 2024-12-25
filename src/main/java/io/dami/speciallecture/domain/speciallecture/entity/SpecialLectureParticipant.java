package io.dami.speciallecture.domain.speciallecture.entity;

import io.dami.speciallecture.domain.user.entity.User;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Comment;

import java.time.LocalDateTime;

@Getter
@Entity
@Comment(value = "특강 참여자")
@Table(
        name = "tb_special_lecture_participant",
        uniqueConstraints = @UniqueConstraint(
                name = "uk_user_lecture_id",
                columnNames = {"user_id","special_lecture_id"}
        )
)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class SpecialLectureParticipant {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "special_lecture_id", nullable = false)
    private SpecialLecture specialLecture;

    @Comment("신청 시간")
    private LocalDateTime createdDate = LocalDateTime.now();

    public SpecialLectureParticipant(User user, SpecialLecture specialLecture) {
        this.user = user;
        this.specialLecture = specialLecture;
        this.createdDate = LocalDateTime.now();
    }
}
