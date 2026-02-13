package com.demo.consultation.domain;

import java.util.List;

public class ConsultationManager {
  private final DataManager dataManager;

  public ConsultationManager(DataManager dataManager) {
    this.dataManager = dataManager;
  }

  public Consultation createConsultation(Patient patient, Doctor doctor, String consultationName) throws DataNotFoundException {
    addPatientToDoctor(patient, doctor);
    return dataManager.createConsultation(consultationName, patient, doctor);
  }

  public List<Consultation> getConsultations() {
    return dataManager.getConsultations();
  }

  public void clear() {
    dataManager.clear();
  }

  public List<Patient> getPatientList() {
    return dataManager.getPatientList();
  }

  public List<Doctor> getDoctorList() {
    return dataManager.getDoctorList();
  }

  public Patient createPatient(String firstName, String lastName) {
    return dataManager.createPatient(firstName, lastName);
  }

  public void deletePatient(String firstName, String lastName) {
    dataManager.deletePatient(firstName, lastName);
  }

  public Doctor createDoctor(String firstName, String lastName) {
    return dataManager.createDoctor(firstName, lastName);
  }

  public void deleteDoctor(String firstName, String lastName) {
    dataManager.deleteDoctor(firstName, lastName);
  }

  public Doctor getDoctorByFirstAndLastName(String firstName, String lastName) throws DataNotFoundException {
    return dataManager.getDoctorByFirstAndLastName(firstName, lastName);
  }

  public Patient getPatientByFirstAndLastName(String firstName, String lastName) throws DataNotFoundException {
    return dataManager.getPatientByFirstAndLastName(firstName, lastName);
  }

  public void addPatientToDoctor(Patient patient, Doctor doctor) throws DataNotFoundException {
    if (doctor.knowsPatient(patient)) {
      return;
    }
    dataManager.addPatientToDoctor(patient, doctor);
  }

  public List<Consultation> getConsultationsByPatient(Patient patient) throws DataNotFoundException {
    return dataManager.getConsultationsByPatient(patient);
  }
}
