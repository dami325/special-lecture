package io.dami.speciallecture.infrastructure.core.speciallecture;

import io.dami.speciallecture.domain.speciallecture.entity.SpecialLecture;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SpecialLectureJpaRepository extends JpaRepository<SpecialLecture, Long>, SpecialLectureJpaRepositoryCustom {


}
