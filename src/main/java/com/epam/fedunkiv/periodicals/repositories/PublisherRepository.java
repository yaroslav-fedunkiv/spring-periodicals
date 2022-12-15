package com.epam.fedunkiv.periodicals.repositories;

import com.epam.fedunkiv.periodicals.model.Publisher;
import com.epam.fedunkiv.periodicals.model.Topics;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface PublisherRepository extends JpaRepository<Publisher, Long> {
    Publisher findByTitle(String title);

    @Modifying
    @Query(value = "select p from Publisher p WHERE p.title LIKE %:title% and p.isActive = true")
    List<Publisher> searchByTitle(@Param("title") String title);

    @Query(value = "select p from Publisher p where p.topic = :topic")
    List<Publisher> findByTopic(Pageable pageable, @Param("topic") Topics topic);
}
