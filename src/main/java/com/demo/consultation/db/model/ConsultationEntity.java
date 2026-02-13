package com.demo.consultation.db.model;

import jakarta.persistence.*;

@Entity
@Table(name = "consultation")
public class ConsultationEntity {
  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private Long id;

  @Column(name = "consultationName", length = 50)
  private String consultationName;

  @ManyToOne
  @JoinColumn(name = "doctor_id", nullable = false)
  private DoctorEntity doctor;

  @ManyToOne
  @JoinColumn(name = "patient_id", nullable = false)
  private PatientEntity patient;
  
  public void setId(Long id) {
    this.id = id;
  }

  public Long getId() {
    return id;
  }

  public String getConsultationName() {
    return consultationName;
  }

  public void setConsultationName(String consultationName) {
    this.consultationName = consultationName;
  }

  public PatientEntity getPatient() {
    return patient;
  }

  public void setPatient(PatientEntity patient) {
    this.patient = patient;
  }

  public DoctorEntity getDoctor() {
    return doctor;
  }

  public void setDoctor(DoctorEntity doctor) {
    this.doctor = doctor;
  }
}
