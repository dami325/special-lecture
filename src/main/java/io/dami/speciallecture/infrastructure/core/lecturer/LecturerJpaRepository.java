package io.dami.speciallecture.infrastructure.core.lecturer;

import io.dami.speciallecture.domain.lecturer.entity.Lecturer;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LecturerJpaRepository extends JpaRepository<Lecturer, Long> {

}
