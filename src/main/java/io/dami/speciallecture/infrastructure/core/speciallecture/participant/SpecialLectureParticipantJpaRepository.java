package io.dami.speciallecture.infrastructure.core.speciallecture.participant;

import io.dami.speciallecture.domain.speciallecture.entity.SpecialLectureParticipant;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SpecialLectureParticipantJpaRepository extends JpaRepository<SpecialLectureParticipant, Long>, SpecialLectureParticipantJpaRepositoryCustom {


}
