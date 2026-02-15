package com.demo.consultation.domain;

import java.util.ArrayList;
import java.util.List;

public class ConsultationManager {
  private final DataManager dataManager;

  public ConsultationManager(DataManager dataManager) {
    this.dataManager = dataManager;
  }

  public Consultation createConsultation(PatientId patientId, DoctorId doctorId, String consultationName) throws DataNotFoundException {
    Patient patient = dataManager.getPatientByFirstAndLastName(patientId.firstName(), patientId.lastName());
    Doctor doctor = dataManager.getDoctorByFirstAndLastName(doctorId.firstName(), doctorId.lastName());
    if (!doctor.knowsPatient(patient)) {
      doctor.addPatient(patient);
      dataManager.save(doctor);
    }
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

  public void addPatientToDoctor(PatientId patientId, DoctorId doctorId) throws DataNotFoundException {
    Patient patient = dataManager.getPatientByFirstAndLastName(patientId.firstName(), patientId.lastName());
    Doctor doctor = dataManager.getDoctorByFirstAndLastName(doctorId.firstName(), doctorId.lastName());
    if (doctor.knowsPatient(patient)) {
      return;
    }
    doctor.addPatient(patient);
    dataManager.save(doctor);
  }

  public List<Consultation> getConsultationsByPatient(String firstName, String lastName) throws DataNotFoundException {
    Patient patient = dataManager.getPatientByFirstAndLastName(firstName, lastName);
    List<Consultation> patientConsultations = new ArrayList<>();
    List<Consultation> consultations = dataManager.getConsultations();
    for (Consultation consultation : consultations) {
      if (consultation.patient().equals(patient)) {
        patientConsultations.add(consultation);
      }
    }
    return patientConsultations;
  }

  public List<Patient> getPatientsAssignedToDoctor(DoctorId doctorId) throws DataNotFoundException {
    Doctor doctor = dataManager.getDoctorByFirstAndLastName(doctorId.firstName(), doctorId.lastName());
    return new ArrayList<>(doctor.getPatients());
  }
}
