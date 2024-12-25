package io.dami.speciallecture.infrastructure.core.lecturer;

import io.dami.speciallecture.domain.lecturer.repository.LecturerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class LecturerRepositoryImpl implements LecturerRepository {

    private final LecturerJpaRepository lecturerJpaRepository;

}
