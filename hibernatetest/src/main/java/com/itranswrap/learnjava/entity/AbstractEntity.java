package com.itranswrap.learnjava.entity;

import jakarta.persistence.*;

import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;

@MappedSuperclass
public class AbstractEntity {
  private Long id;
  private Long createAt;

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(nullable = false, updatable = false)
  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  @Column(nullable = false, updatable = false)
  public Long getCreateAt() {
    return createAt;
  }

  public void setCreateAt(Long createAt) {
    this.createAt = createAt;
  }
  @Transient
  public ZonedDateTime getCreatedDateTime() {
    return Instant.ofEpochMilli(this.createAt).atZone(ZoneId.systemDefault());
  }
  @PrePersist
  public void preInsert() {
    setCreateAt(System.currentTimeMillis());
  }
}
