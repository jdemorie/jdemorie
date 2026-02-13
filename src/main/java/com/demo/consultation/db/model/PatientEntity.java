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

  @ManyToOne
  @JoinColumn(name = "doctor_id")
  private DoctorEntity doctor;

  public PatientEntity() {
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public DoctorEntity getDoctor() {
    return doctor;
  }

  public void setDoctor(DoctorEntity borrower) {
    this.doctor = borrower;
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
