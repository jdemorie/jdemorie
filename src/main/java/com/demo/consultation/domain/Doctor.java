package com.demo.consultation.domain;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public final class Doctor {
  private final String firstName;
  private final String lastName;
  private final List<Patient> patients;

  public Doctor(String firstName, String lastName) {
    this.firstName = firstName;
    this.lastName = lastName;
    this.patients = new ArrayList<>();
  }

  public String firstName() {
    return firstName;
  }

  public String lastName() {
    return lastName;
  }

  public List<Patient> getPatients() {
    return Collections.unmodifiableList(patients);
  }

  public void addPatient(Patient patient) {
    patients.add(patient);
  }

  @Override
  public boolean equals(Object obj) {
    if (obj == this) {
      return true;
    }
    if (obj == null || obj.getClass() != this.getClass()) {
      return false;
    }
    var that = (Doctor) obj;
    return Objects.equals(this.firstName, that.firstName) &&
           Objects.equals(this.lastName, that.lastName);
  }

  @Override
  public int hashCode() {
    return Objects.hash(firstName, lastName);
  }

  @Override
  public String toString() {
    return "Person[" +
           "firstName=" + firstName + ", " +
           "lastName=" + lastName + ']';
  }

  public boolean knowsPatient(Patient patient) {
    return patients.contains(patient);
  }
}
