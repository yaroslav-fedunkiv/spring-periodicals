package com.epam.fedunkiv.periodicals.repositories;

import com.epam.fedunkiv.periodicals.model.Subscriptions;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SubscriptionRepository extends JpaRepository<Subscriptions, Long> {
}
