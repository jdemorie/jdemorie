package com.demo.consultation.domain;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class FakeDocManager implements DataManager {
  private final List<Patient> patients = new ArrayList<>();
  private final List<Doctor> doctors = new ArrayList<>();
  private final List<Consultation> consultations = new ArrayList<>();

  @Override
  public Patient createPatient(String firstName, String lastName) {
    Patient patient = new Patient(firstName, lastName);
    patients.add(patient);
    return patient;
  }

  @Override
  public void deletePatient(String firstName, String lastName) {
    getPatient(firstName, lastName).ifPresent(patients::remove);
  }

  private Optional<Patient> getPatient(String firstName, String lastName) {
    return patients.stream().filter(patient -> patient.firstName().equals(firstName) && patient.lastName().equals(lastName)).findFirst();
  }

  @Override
  public List<Patient> getPatientList() {
    return Collections.unmodifiableList(patients);
  }

  @Override
  public List<Doctor> getDoctorList() {
    return Collections.unmodifiableList(doctors);
  }

  @Override
  public void clear() {
    consultations.clear();
    patients.clear();
    doctors.clear();
  }

  @Override
  public Doctor createDoctor(String firstName, String lastName) {
    Doctor doctor = new Doctor(firstName, lastName);
    doctors.add(doctor);
    return doctor;
  }

  @Override
  public void deleteDoctor(String firstName, String lastName) {
    getDoctor(firstName, lastName).ifPresent(doctors::remove);
  }

  @Override
  public Consultation createConsultation(String consultationName, Patient patient, Doctor doctor) {
    Consultation consultation = new Consultation(consultationName, patient, doctor);
    consultations.add(consultation);
    return consultation;
  }

  private Optional<Doctor> getDoctor(String firstName, String lastName) {
    return doctors.stream().filter(doctor -> doctor.firstName().equals(firstName) && doctor.lastName().equals(lastName)).findFirst();
  }

  @Override
  public Doctor getDoctorByFirstAndLastName(String firstName, String lastName) throws DataNotFoundException {
    for (Doctor doctor : doctors) {
      if (doctor.firstName().equals(firstName) && doctor.lastName().equals(lastName)) {
        return doctor;
      }
    }
    throw new DataNotFoundException("Doctor with name " + firstName + " " + lastName + " not found");
  }

  @Override
  public Patient getPatientByFirstAndLastName(String firstName, String lastName) throws DataNotFoundException {
    for (Patient patient : patients) {
      if (patient.firstName().equals(firstName) && patient.lastName().equals(lastName)) {
        return patient;
      }
    }
    throw new DataNotFoundException("Patient with name " + firstName + " " + lastName + " not found");
  }

  @Override
  public List<Consultation> getConsultationsByPatient(Patient patient) {
    return consultations.stream().filter(consultation -> consultation.patient().equals(patient)).collect(Collectors.toList());
  }

  @Override
  public void addPatientToDoctor(Patient patient, Doctor doctor) throws DataNotFoundException {
    doctor.addPatient(patient);
  }

  @Override
  public List<Consultation> getConsultations() {
    return Collections.unmodifiableList(consultations);
  }
}
