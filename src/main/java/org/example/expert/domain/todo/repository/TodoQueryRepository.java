package org.example.expert.domain.todo.repository;

import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.example.expert.domain.todo.dto.response.TodoResponse;
import org.example.expert.domain.user.dto.response.UserResponse;
import org.springframework.stereotype.Repository;

import static org.example.expert.domain.todo.entity.QTodo.todo;
import static org.example.expert.domain.user.entity.QUser.user;

@Repository
@RequiredArgsConstructor
public class TodoQueryRepository {

    private final JPAQueryFactory queryFactory;

    public TodoResponse searchTodo(Long todoIdCond) {

        // DTO로 필요한 값만 응답해서 최적화 + 엔티티 보호
        return queryFactory
                .select(Projections.constructor(TodoResponse.class,
                        todo.id,
                        todo.title,
                        todo.contents,
                        todo.weather,
                        Projections.constructor(UserResponse.class,
                                user.id,
                                user.email),
                        todo.createdAt,
                        todo.modifiedAt))
                .from(todo)
                .join(todo.user, user)
                .where(eqTodoId(todoIdCond))
                .fetchOne();
    }

    // 단 건 조회에서는 특수한 상황 제외하고는 동적 쿼리를 쓸 일이 별로 없음
    private BooleanExpression eqTodoId(Long todoIdCond) {

        return todoIdCond != null ? todo.id.eq(todoIdCond) : null;
    }
}
