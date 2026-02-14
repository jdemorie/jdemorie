package com.demo.consultation.domain;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public final class Doctor {
  private final DoctorId doctorId;
  private final List<Patient> patients;

  public Doctor(String firstName, String lastName) {
    this.doctorId = new DoctorId(firstName, lastName);
    this.patients = new ArrayList<>();
  }

  public DoctorId doctorId() {
    return doctorId;
  }

  public List<Patient> getPatients() {
    return Collections.unmodifiableList(patients);
  }

  public void addPatient(Patient patient) {
    patients.add(patient);
  }

  @Override
  public boolean equals(Object o) {
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    Doctor doctor = (Doctor) o;
    return Objects.equals(doctorId, doctor.doctorId);
  }

  @Override
  public int hashCode() {
    return Objects.hashCode(doctorId);
  }

  public boolean knowsPatient(Patient patient) {
    return patients.contains(patient);
  }
}
