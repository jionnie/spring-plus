package org.example.expert.domain.todo.repository;

import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.example.expert.domain.todo.dto.response.TodoResponse;
import org.example.expert.domain.todo.dto.response.TodoSearchResponse;
import org.example.expert.domain.user.dto.response.UserResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;

import static org.example.expert.domain.comment.entity.QComment.comment;
import static org.example.expert.domain.manager.entity.QManager.manager;
import static org.example.expert.domain.todo.entity.QTodo.todo;
import static org.example.expert.domain.user.entity.QUser.user;

@Repository
@RequiredArgsConstructor
public class TodoQueryRepository {

    private final JPAQueryFactory queryFactory;

    public TodoResponse searchTodo(Long todoId) {

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
                .where(eqTodoId(todoId))
                .fetchOne();
    }

    // 단 건 조회에서는 특수한 상황 제외하고는 동적 쿼리를 쓸 일이 별로 없음
    private BooleanExpression eqTodoId(Long todoId) {

        return todoId != null ? todo.id.eq(todoId) : null;
    }

    // Level 3: QueryDSL을 이용한 검색 기능 구현
    public Page<TodoSearchResponse> searchTodos(
            String keyword,
            LocalDateTime since,
            LocalDateTime until,
            String nickname,
            Pageable pageable) {

        Long total = queryFactory
                .select(todo.countDistinct())
                .from(todo)
                .leftJoin(todo.managers, manager)
                .leftJoin(manager.user, user)
                .where(
                        containsKeyword(keyword),
                        createdAtSince(since),
                        createdAtUntil(until),
                        containsNickname(nickname))
                .fetchOne();

        long totalCount = (total != null) ? total : 0L;

        return new PageImpl<>(
                queryFactory
                        .select(Projections.constructor(TodoSearchResponse.class,
                                todo.title,
                                JPAExpressions.select(manager.countDistinct())
                                        .from(manager)
                                        .where(manager.todo.eq(todo)),
                                JPAExpressions.select(comment.countDistinct())
                                        .from(comment)
                                        .where(comment.todo.eq(todo))
                                ))
                        .from(todo)
                        .leftJoin(todo.managers, manager)
                        .leftJoin(manager.user, user)
                        .where(
                                containsKeyword(keyword),
                                createdAtSince(since),
                                createdAtUntil(until),
                                containsNickname(nickname)
                        )
                        .orderBy(todo.createdAt.desc())
                        .offset(pageable.getOffset())
                        .limit(pageable.getPageSize())
                        .fetch(),
                pageable,
                totalCount
        );
    }

    private BooleanExpression containsKeyword(String keyword) {

        return (keyword != null) ? todo.title.contains(keyword) : null;
    }

    private BooleanExpression createdAtSince(LocalDateTime since) {

        return (since != null) ? todo.createdAt.goe(since) : null;
    }

    private BooleanExpression createdAtUntil(LocalDateTime until) {

        return (until != null) ? todo.createdAt.before(until) : null;
    }

    private BooleanExpression containsNickname(String nickname) {

        return (nickname != null) ? user.nickname.contains(nickname) : null;
    }
}
