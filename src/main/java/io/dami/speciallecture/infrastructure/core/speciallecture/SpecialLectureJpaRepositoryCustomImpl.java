package io.dami.speciallecture.infrastructure.core.speciallecture;

import com.querydsl.jpa.impl.JPAQueryFactory;
import io.dami.speciallecture.domain.speciallecture.command.SpecialLecturesCommand;
import io.dami.speciallecture.domain.speciallecture.entity.QSpecialLectureParticipant;
import io.dami.speciallecture.domain.speciallecture.entity.SpecialLecture;
import jakarta.persistence.LockModeType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

import static io.dami.speciallecture.domain.lecturer.entity.QLecturer.lecturer;
import static io.dami.speciallecture.domain.speciallecture.entity.QSpecialLecture.specialLecture;
import static io.dami.speciallecture.domain.speciallecture.entity.QSpecialLectureParticipant.*;
import static io.dami.speciallecture.domain.user.entity.QUser.user;

@Component
@RequiredArgsConstructor
public class SpecialLectureJpaRepositoryCustomImpl implements SpecialLectureJpaRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public List<SpecialLecture> getSpecialLecturePage(SpecialLecturesCommand command) {
        return queryFactory
                .selectFrom(specialLecture)
                .innerJoin(specialLecture.lecturer, lecturer).fetchJoin()
                .innerJoin(lecturer.user, user).fetchJoin()
                .where(
                        specialLecture.participationStartTime.before(command.from()),
                        specialLecture.participationEndTime.after(command.to())
                )
                .orderBy(specialLecture.id.desc())
                .offset(command.pageable().getOffset())
                .limit(command.pageable().getPageSize())
                .fetch();
    }

    @Override
    public long getSpecialLecturePageCount(SpecialLecturesCommand command) {
        return queryFactory
                .select(specialLecture.id.count())
                .from(specialLecture)
                .where(
                        specialLecture.participationStartTime.before(command.from()),
                        specialLecture.participationEndTime.after(command.to())
                )
                .fetchOne();
    }

    @Override
    public Optional<SpecialLecture> selectForUpdateById(Long id) {
        return Optional.ofNullable(
                queryFactory.selectFrom(specialLecture)
                        .innerJoin(specialLecture.lecturer, lecturer).fetchJoin()
                        .innerJoin(lecturer.user, user).fetchJoin()
                        .where(
                                specialLecture.id.eq(id)
                        )
                        .setLockMode(LockModeType.PESSIMISTIC_WRITE)
                        .fetchOne());
    }

    @Override
    public Optional<SpecialLecture> findFetchById(Long id) {
        return Optional.ofNullable(
                queryFactory.selectFrom(specialLecture)
                        .innerJoin(specialLecture.lecturer, lecturer).fetchJoin()
                        .innerJoin(lecturer.user, user).fetchJoin()
                        .leftJoin(specialLecture.specialLectureParticipants, specialLectureParticipant).fetchJoin()
                        .where(
                                specialLecture.id.eq(id)
                        )
                        .fetchOne());
    }
}
