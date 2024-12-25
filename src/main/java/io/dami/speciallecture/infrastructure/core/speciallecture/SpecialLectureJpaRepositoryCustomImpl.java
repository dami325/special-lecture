package io.dami.speciallecture.infrastructure.core.speciallecture;

import com.querydsl.jpa.impl.JPAQueryFactory;
import io.dami.speciallecture.domain.speciallecture.command.SpecialLecturesCommand;
import io.dami.speciallecture.domain.speciallecture.entity.SpecialLecture;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

import static io.dami.speciallecture.domain.lecturer.entity.QLecturer.*;
import static io.dami.speciallecture.domain.speciallecture.entity.QSpecialLecture.*;
import static io.dami.speciallecture.domain.speciallecture.entity.QSpecialLectureParticipant.*;
import static io.dami.speciallecture.domain.user.entity.QUser.*;

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
                .leftJoin(specialLecture.specialLectureParticipants, specialLectureParticipant).fetchJoin()
                .where(
                        specialLecture.participationStartTime.after(command.from()),
                        specialLecture.participationEndTime.before(command.to())
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
                        specialLecture.participationStartTime.after(command.from()),
                        specialLecture.participationEndTime.before(command.to())
                )
                .fetchOne();
    }
}
