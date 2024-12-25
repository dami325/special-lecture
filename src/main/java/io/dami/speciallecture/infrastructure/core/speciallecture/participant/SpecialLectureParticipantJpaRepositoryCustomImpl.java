package io.dami.speciallecture.infrastructure.core.speciallecture.participant;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import io.dami.speciallecture.domain.speciallecture.command.ParticipantsCommand;
import io.dami.speciallecture.domain.speciallecture.entity.SpecialLectureParticipant;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

import static io.dami.speciallecture.domain.speciallecture.entity.QSpecialLecture.*;
import static io.dami.speciallecture.domain.speciallecture.entity.QSpecialLectureParticipant.*;
import static io.dami.speciallecture.domain.user.entity.QUser.*;

@Component
@RequiredArgsConstructor
public class SpecialLectureParticipantJpaRepositoryCustomImpl implements SpecialLectureParticipantJpaRepositoryCustom{

    private final JPAQueryFactory queryFactory;

    @Override
    public List<SpecialLectureParticipant> getParticipantPage(ParticipantsCommand command) {
        return queryFactory
                .selectFrom(specialLectureParticipant)
                .innerJoin(specialLectureParticipant.user, user).fetchJoin()
                .innerJoin(specialLectureParticipant.specialLecture, specialLecture).fetchJoin()
                .where(
                        userIdEq(command.userId())
                )
                .orderBy(specialLectureParticipant.id.desc())
                .offset(command.pageable().getOffset())
                .limit(command.pageable().getPageSize())
                .fetch();
    }

    @Override
    public long getParticipantPageCount(ParticipantsCommand command) {
        return queryFactory
                .select(specialLectureParticipant.id.count())
                .from(specialLectureParticipant)
                .innerJoin(specialLectureParticipant.user, user)
                .where(
                        userIdEq(command.userId())
                )
                .fetchOne();
    }

    private static BooleanExpression userIdEq(Long userId) {
        return specialLectureParticipant.user.id.eq(userId);
    }
}
