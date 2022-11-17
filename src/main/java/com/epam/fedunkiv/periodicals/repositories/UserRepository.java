package com.epam.fedunkiv.periodicals.repositories;

import com.epam.fedunkiv.periodicals.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface UserRepository extends JpaRepository<User, Long> {
    User findByEmail(String email);

    @Modifying
    @Query(value = "update user set is_active = false where email = ?1", nativeQuery = true)
    void deactivateUser(String email);

    @Modifying
    @Query(value = "update User set full_name = :name, email = :newEmail, address = :address where email = :oldEmail",
    nativeQuery = true)
    void updateUser(@Param("oldEmail") String oldEmail, @Param("name")String fullName,
                    @Param("newEmail")String newEmail, @Param("address") String address);

    @Modifying
    @Query(value = "update user set balance = ?1 where email = ?2", nativeQuery = true)
    int updateBalance(Double newBalance, String email);
}
