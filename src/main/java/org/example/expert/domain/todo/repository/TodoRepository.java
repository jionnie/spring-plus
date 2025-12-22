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
           JOIN FETCH t.user
           WHERE (:weather IS NULL OR t.weather = :weather) AND
                 (:since IS NULL OR t.modifiedAt >= :since) AND
                 (:until IS NULL OR t.modifiedAt < :until)
           ORDER BY t.modifiedAt DESC
           """)
    Page<Todo> findWithOptions(
            @Param("weather") String weather,
            @Param("since") LocalDateTime since,
            @Param("until") LocalDateTime until,
            Pageable pageable);
}
