package com.progmatic.store.auth.repository;

import com.progmatic.store.auth.entity.AuthUserCredentials;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AuthUserRepository extends JpaRepository<AuthUserCredentials, UUID> {}
