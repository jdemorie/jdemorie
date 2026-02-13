package com.demo.consultation.db.model;

import jakarta.persistence.*;

@Entity
@Table(name = "patient")
public class PatientEntity {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(name = "firstName", length = 50)
  private String firstName;

  @Column(name = "lastName", length = 50)
  private String lastName;

  public PatientEntity() {
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getLastName() {
    return lastName;
  }

  public void setLastName(String lastName) {
    this.lastName = lastName;
  }

  public String getFirstName() {
    return firstName;
  }

  public void setFirstName(String firstName) {
    this.firstName = firstName;
  }
}
