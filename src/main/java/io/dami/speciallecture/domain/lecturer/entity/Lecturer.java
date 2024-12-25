package io.dami.speciallecture.domain.lecturer.entity;

import io.dami.speciallecture.domain.speciallecture.entity.SpecialLecture;
import io.dami.speciallecture.domain.user.entity.User;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Comment;

import java.util.ArrayList;
import java.util.List;

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
    @JoinColumn(name = "user_id", nullable = false, unique = true)
    @Comment("사용자")
    private User user;

    @Comment("소개")
    private String introduction;

    @Comment("소속")
    private String organization;

    @OneToMany(mappedBy = "lecturer", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<SpecialLecture> specialLectures = new ArrayList<>();
}
