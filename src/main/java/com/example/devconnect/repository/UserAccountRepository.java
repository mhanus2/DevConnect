package com.example.devconnect.repository;


import com.example.devconnect.model.UserAccount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserAccountRepository extends JpaRepository<UserAccount, Integer> {
    Optional<UserAccount> findByUsername(String username);
    Optional<UserAccount> findById(Integer id);

}
