package com.epam.fedunkiv.periodicals.repositories;

import com.epam.fedunkiv.periodicals.model.Publisher;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PublisherRepository extends JpaRepository<Publisher, Long> {
    Publisher getByTitle(String title);
    void deleteByTitle(String title);

}
