package com.progmatic.auth.repository;

import com.progmatic.auth.config.SpringPersistenceConfiguration;
import com.progmatic.auth.entity.UserCredential;
import jakarta.validation.*;
import java.util.Optional;
import java.util.UUID;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.boot.jdbc.test.autoconfigure.AutoConfigureTestDatabase;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.orm.jpa.JpaSystemException;
import org.springframework.test.context.ContextConfiguration;
import org.testcontainers.postgresql.PostgreSQLContainer;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ContextConfiguration(classes = {SpringPersistenceConfiguration.class})
public class UserManagementRepositoryTest {

  @Autowired private UserManagementRepository userManagementRepository;

  @ServiceConnection
  private static final PostgreSQLContainer postgres =
      new PostgreSQLContainer("postgres:16.11")
          .withDatabaseName("auth-db")
          .withUsername("test_user_rw")
          .withPassword("test_user_password")
          .withInitScript("data/sql/create-schema-auth.sql");

  @Test
  public void testTableDataIntegrity_withNullUserId() {
    UserCredential userCredential =
        new UserCredential.Builder().email("test1@gmail.com").hashedPassword("password").build();
    Assertions.assertThatExceptionOfType(JpaSystemException.class)
        .isThrownBy(() -> userManagementRepository.save(userCredential));
  }

  @Test
  public void testTableDataIntegrity_withNullUserIdAndNullEmail() {
    UserCredential userCredential = new UserCredential.Builder().hashedPassword("password").build();
    Assertions.assertThatExceptionOfType(JpaSystemException.class)
        .isThrownBy(() -> userManagementRepository.save(userCredential));
  }

  @Test
  public void testTableDataIntegrity_withNullEmail() {
    UserCredential userCredential =
        new UserCredential.Builder().userId(UUID.randomUUID()).hashedPassword("password").build();
    Assertions.assertThatThrownBy(() -> userManagementRepository.saveAndFlush(userCredential))
        .isInstanceOf(ConstraintViolationException.class);
    Assertions.assertThatExceptionOfType(ConstraintViolationException.class)
        .isThrownBy(
            () -> {
              userManagementRepository.saveAndFlush(userCredential);
            })
        .satisfies(
            e ->
                Assertions.assertThat(e.getConstraintViolations())
                    .extracting(ConstraintViolation::getMessage)
                    .containsExactlyInAnyOrder("email is an empty string."));
  }

  @Test
  public void testTableDataIntegrity_withBlankEmail() {
    UserCredential userCredential =
        new UserCredential.Builder()
            .userId(UUID.randomUUID())
            .email("")
            .hashedPassword("password")
            .build();
    Assertions.assertThatExceptionOfType(ConstraintViolationException.class)
        .isThrownBy(
            () -> {
              userManagementRepository.saveAndFlush(userCredential);
            })
        .satisfies(
            e ->
                Assertions.assertThat(e.getConstraintViolations())
                    .extracting(ConstraintViolation::getMessage)
                    .containsExactlyInAnyOrder("email is an empty string."));
  }

  @Test
  public void testTableDataIntegrity_withNullPassword() {
    UserCredential userCredential =
        new UserCredential.Builder().userId(UUID.randomUUID()).email("test1@gmail.com").build();
    Assertions.assertThatThrownBy(() -> userManagementRepository.saveAndFlush(userCredential))
        .isInstanceOf(ConstraintViolationException.class);
    Assertions.assertThatExceptionOfType(ConstraintViolationException.class)
        .isThrownBy(
            () -> {
              userManagementRepository.saveAndFlush(userCredential);
            })
        .satisfies(
            e ->
                Assertions.assertThat(e.getConstraintViolations())
                    .extracting(ConstraintViolation::getMessage)
                    .containsExactlyInAnyOrder("hashed_password is an empty string."));
  }

  @Test
  public void testTableDataIntegrity_withBlankPassword() {
    UserCredential userCredential =
        new UserCredential.Builder()
            .userId(UUID.randomUUID())
            .email("test1@gmail.com")
            .hashedPassword("")
            .build();
    Assertions.assertThatExceptionOfType(ConstraintViolationException.class)
        .isThrownBy(
            () -> {
              userManagementRepository.saveAndFlush(userCredential);
            })
        .satisfies(
            e ->
                Assertions.assertThat(e.getConstraintViolations())
                    .extracting(ConstraintViolation::getMessage)
                    .containsExactlyInAnyOrder("hashed_password is an empty string."));
  }

  @Test
  public void testTableDataIntegrity_withNullEmailAndPassword() {
    UserCredential userCredential = new UserCredential.Builder().userId(UUID.randomUUID()).build();
    Assertions.assertThatExceptionOfType(ConstraintViolationException.class)
        .isThrownBy(
            () -> {
              userManagementRepository.saveAndFlush(userCredential);
            })
        .satisfies(
            e ->
                Assertions.assertThat(e.getConstraintViolations())
                    .extracting(ConstraintViolation::getMessage)
                    .containsExactlyInAnyOrder(
                        "hashed_password is an empty string.", "email is an empty string."));
  }

  @Test
  public void testTableDataIntegrity_withBlankEmailAndPassword() {
    UserCredential userCredential =
        new UserCredential.Builder().userId(UUID.randomUUID()).email("").hashedPassword("").build();
    Assertions.assertThatExceptionOfType(ConstraintViolationException.class)
        .isThrownBy(
            () -> {
              userManagementRepository.saveAndFlush(userCredential);
            })
        .satisfies(
            e ->
                Assertions.assertThat(e.getConstraintViolations())
                    .extracting(ConstraintViolation::getMessage)
                    .containsExactlyInAnyOrder(
                        "hashed_password is an empty string.", "email is an empty string."));
  }

  @Test
  public void testFindByEmail_whenEmailExist() {
    UserCredential userCredential =
        new UserCredential.Builder()
            .userId(UUID.randomUUID())
            .email("test1@gmail.com")
            .hashedPassword("hashedPassword")
            .build();
    userManagementRepository.save(userCredential);

    Optional<UserCredential> queriedUser = userManagementRepository.findByEmail("test1@gmail.com");
    Assertions.assertThat(queriedUser).isPresent();
    Assertions.assertThat(queriedUser.get().getEmail()).isEqualTo("test1@gmail.com");
  }

  @Test
  public void testFindByEmail_whenEmailDoesNotExist() {
    Optional<UserCredential> queriedUser = userManagementRepository.findByEmail("test1@gmail.com");
    Assertions.assertThat(queriedUser).isEmpty();
  }
}
