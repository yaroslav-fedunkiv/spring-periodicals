package com.epam.fedunkiv.periodicals.repositories;

import com.epam.fedunkiv.periodicals.model.Publisher;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface PublisherRepository extends JpaRepository<Publisher, Long> {
    Publisher getByTitle(String title);
    void deleteByTitle(String title);

    @Modifying
    @Query(value = "update publisher set is_active = false where title = ?1", nativeQuery = true)
    void deactivatePublisher(String title);
}
