package com.demo.consultation.db.model;

import jakarta.persistence.*;

import java.util.List;

@Entity
@Table(name = "doctor")
public class DoctorEntity {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(name = "firstName", length = 50)
  private String firstName;

  @Column(name = "lastName", length = 50)
  private String lastName;

  @OneToMany(mappedBy = "doctor", cascade = CascadeType.ALL, orphanRemoval = true)
  private List<PatientEntity> patients;

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getFirstName() {
    return firstName;
  }

  public void setFirstName(String firstName) {
    this.firstName = firstName;
  }

  public String getLastName() {
    return lastName;
  }

  public void setLastName(String lastName) {
    this.lastName = lastName;
  }

  public List<PatientEntity> getPatients() {
    return patients;
  }

  public void setPatients(List<PatientEntity> borrowedBooks) {
    this.patients = borrowedBooks;
  }
}
