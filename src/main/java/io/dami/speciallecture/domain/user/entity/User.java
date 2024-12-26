package io.dami.speciallecture.domain.user.entity;

import io.dami.speciallecture.domain.speciallecture.entity.SpecialLectureParticipant;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Comment;

import java.util.HashSet;
import java.util.Set;

@Getter
@Entity
@Comment(value = "사용자")
@Table(name = "tb_user")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String username;

    @OneToMany(mappedBy = "user", cascade = {CascadeType.PERSIST,CascadeType.MERGE}, orphanRemoval = true)
    private Set<SpecialLectureParticipant> specialLectureParticipants = new HashSet<>();

    public User(Long id, String username) {
        this.id = id;
        this.username = username;
        this.specialLectureParticipants = new HashSet<>();
    }
}
