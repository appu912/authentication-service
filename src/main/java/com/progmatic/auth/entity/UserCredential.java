package com.progmatic.auth.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.Instant;
import java.util.UUID;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

@Data
@NoArgsConstructor
@Entity
@Table(name = "user_credentials", schema = "auth")
public class UserCredential {
  @NotNull(message = "user_id is null.") @Id
  @Column(name = "user_id")
  private UUID userId;

  @NotBlank(message = "email is an empty string.") @Column(name = "email", unique = true, nullable = false)
  private String email;

  @NotBlank(message = "hashed_password is an empty string.") @Column(name = "hashed_password", nullable = false)
  private String hashedPassword;

  @Column(name = "created_at", nullable = false, updatable = false)
  @CreationTimestamp
  private Instant createdAt;

  @Column(name = "updated_at", nullable = false)
  @UpdateTimestamp
  private Instant updatedAt;

  private UserCredential(Builder b) {
    this.userId = b.userId;
    this.email = b.email;
    this.hashedPassword = b.hashedPassword;
    this.createdAt = b.createdAt;
    this.updatedAt = b.updatedAt;
  }

  public static class Builder {
    private UUID userId;
    private String email;
    private String hashedPassword;
    private Instant createdAt;
    private Instant updatedAt;

    public Builder userId(UUID userId) {
      this.userId = userId;
      return this;
    }

    public Builder email(String email) {
      this.email = email;
      return this;
    }

    public Builder hashedPassword(String hashedPassword) {
      this.hashedPassword = hashedPassword;
      return this;
    }

    public Builder createdAt(Instant createdAt) {
      this.createdAt = createdAt;
      return this;
    }

    public Builder updatedAt(Instant updatedAt) {
      this.updatedAt = updatedAt;
      return this;
    }

    public UserCredential build() {
      return new UserCredential(this);
    }
  }
}
