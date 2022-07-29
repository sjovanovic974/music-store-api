package com.example.demo.security.repository;

import com.example.demo.security.model.PasswordResetToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PasswordResetTokenRepository extends JpaRepository<PasswordResetToken, Long> {
     PasswordResetToken findByToken(String token);
     void deleteByToken(String token);
}
