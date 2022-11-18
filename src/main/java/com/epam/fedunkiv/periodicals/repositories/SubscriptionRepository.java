package com.epam.fedunkiv.periodicals.repositories;

import com.epam.fedunkiv.periodicals.model.Subscriptions;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;


public interface SubscriptionRepository extends JpaRepository<Subscriptions, Long> {
    List<Subscriptions> findByPublisherIdAndUserId(Long publisherId, Long userId);
}
