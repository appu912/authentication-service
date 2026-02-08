package com.progmatic.auth.repository;

import com.progmatic.auth.entity.UserCredential;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserManagementRepository extends JpaRepository<UserCredential, UUID> {
  Optional<UserCredential> findByEmail(String email);
}
