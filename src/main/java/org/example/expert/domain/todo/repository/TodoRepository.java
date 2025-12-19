package org.example.expert.domain.todo.repository;

import org.example.expert.domain.todo.entity.Todo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;

public interface TodoRepository extends JpaRepository<Todo, Long> {

    @Query("""
           SELECT t
           FROM Todo t
           JOIN User u ON t.user.id = u.id
           WHERE (:weather IS NULL OR t.weather = :weather) AND
                 (:startDate IS NULL OR t.modifiedAt >= :startDate) AND
                 (:endDate IS NULL OR t.modifiedAt < :endDate)
           ORDER BY t.modifiedAt DESC
           """)
    Page<Todo> findWithOptions(
            @Param("weather") String weather,
            @Param("startDate") LocalDateTime startDate,
            @Param("endDate") LocalDateTime endDate,
            Pageable pageable);
}
