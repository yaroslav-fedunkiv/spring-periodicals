package com.epam.fedunkiv.periodicals.repositories;

import com.epam.fedunkiv.periodicals.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface UserRepository extends JpaRepository<User, Long> {
    User findByEmail(String email);
    void deleteByEmail(String email);

    @Modifying
    @Query(value = "update user set balance = ?1 where email = ?2", nativeQuery = true)
    int updateBalance(Double newBalance, String email);
}
